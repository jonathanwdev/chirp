package com.plcoding.chat.data.network

import com.plcoding.chat.data.dto.websocket.WebSocketMessageDTO
import com.plcoding.chat.data.lifecycle.AppLifecycleObserver
import com.plcoding.chat.domain.error.ConnectionError
import com.plcoding.chat.domain.models.ConnectionState
import com.plcoding.core.data.networking.UrlConstants
import com.plcoding.core.domain.auth.SessionStorage
import com.plcoding.core.domain.logging.ChirpLogger
import com.plcoding.core.domain.util.EmptyResult
import com.plcoding.core.domain.util.Result
import com.plcoding.feature.chat.data.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.header
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration.Companion.seconds

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
class KtorWebSocketConnector(
    private val httpClient: HttpClient,
    private val applicationScope: CoroutineScope,
    private val sessionStorage: SessionStorage,
    private val json: Json,
    private val connectionErrorHandler: ConnectionErrorHandler,
    private val connectionRetryHandler: ConnectionRetryHandler,
    private val appLifecycleObserver: AppLifecycleObserver,
    private val connectivityObserver: ConnectivityObserver,
    private val logger: ChirpLogger
) {
    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)
    val connectionState = _connectionState.asStateFlow()

    private var currentSession: WebSocketSession? = null

    private val isConnected = connectivityObserver
        .isConnected
        .debounce(1.seconds)
        .stateIn(
            applicationScope,
            SharingStarted.WhileSubscribed(5000L),
            false
        )
    private val isInForeground = appLifecycleObserver
        .isInForeground
        .onEach {  isInForeground ->
            if(isInForeground) {
                connectionRetryHandler.resetDelay()
            }
        }
        .stateIn(
            applicationScope,
            SharingStarted.WhileSubscribed(5000L),
            false
        )

    private suspend fun closeConnectionAndClear() {
        currentSession?.close()
        currentSession = null
    }

    val messages = combine(
        sessionStorage.observeAuthInfo(),
        isConnected,
        isInForeground
    ) { authInfo, isConnected, isInForeground ->
        when {
            authInfo == null -> {
                logger.info("No authentication details. Clearing session and disconnecting...")
                _connectionState.value = ConnectionState.DISCONNECTED
                closeConnectionAndClear()
                connectionRetryHandler.resetDelay()
                null
            }
            !isInForeground -> {
                logger.info("App is not in foreground, disconnecting socket proactively")
                _connectionState.value = ConnectionState.DISCONNECTED
                closeConnectionAndClear()
                null
            }
            !isConnected -> {
                logger.info("Device is disconnected, closing socket connection")
                _connectionState.value = ConnectionState.ERROR_NETWORK
                closeConnectionAndClear()
                null
            }
            else -> {
                logger.info("Device in foreground and connected. Establishing connection")
                if(
                    _connectionState.value !in listOf(ConnectionState.CONNECTING, ConnectionState.CONNECTED)
                ) {
                    _connectionState.value = ConnectionState.CONNECTING
                }

                authInfo
            }
        }

    }.flatMapLatest { authInfo ->
        if(authInfo == null) {
            emptyFlow()
        }else {
            createWebSocketFlow(authInfo.accessToken)
                // Catch block to transform exception for platform compatibility
                .catch { err ->
                    logger.error("Exception in WebSocket.", err)
                    closeConnectionAndClear()
                    val transformException = connectionErrorHandler.transformException(err)
                    throw transformException
                }
                // retry connection add timeout and calculate attempts
                .retryWhen { t, attempt ->
                    logger.info("Connection failed on attempt $attempt")
                    val shouldRetry = connectionRetryHandler.shouldRetry(t, attempt)
                    if(shouldRetry) {
                        _connectionState.value = ConnectionState.CONNECTING
                        connectionRetryHandler.applyRetryDelay(attempt)
                    }
                    shouldRetry
                }
                // Catch block for non retryable errors
                .catch { e ->
                    logger.error("Unhandled WebSocket error", e)
                    _connectionState.value = connectionErrorHandler.getConnectionStateForError(e)

                }
        }
    }

    private fun createWebSocketFlow(accessToken: String) = callbackFlow {
        _connectionState.value = ConnectionState.CONNECTING
        currentSession = httpClient.webSocketSession(
            urlString = "${UrlConstants.BASE_URL_WS}/chat"
        ) {
            header("Authorization", "Bearer $accessToken")
            header("X-API-KEY", BuildKonfig.API_KEY)
        }

        currentSession?.let { session ->
            _connectionState.value = ConnectionState.CONNECTED
            session
                .incoming
                .consumeAsFlow()
                .buffer(
                    capacity = 100
                )
                .collect { frame ->
                    when(frame) {
                        is Frame.Text -> {
                            val text = frame.readText()
                            logger.info("Received row text frame: $text")
                            val messageDto = json.decodeFromString<WebSocketMessageDTO>(text)
                            send(messageDto)
                        }
                        is Frame.Ping -> {
                            logger.debug("Received ping from the server. Sending pong...")
                            session.send(Frame.Pong(frame.data))
                        }
                        else -> Unit
                    }
                }
        } ?: throw Exception("Failed to establish websocket connection")

        awaitClose {
            launch(NonCancellable) {
                logger.info("Disconnecting from websocket session")
                _connectionState.value = ConnectionState.DISCONNECTED
                closeConnectionAndClear()
            }

        }
    }

    suspend fun sendMessage(message: String): EmptyResult<ConnectionError> {
        val connectionState = connectionState.value
        if(currentSession == null  || connectionState != ConnectionState.CONNECTED) {
            return Result.Failure(ConnectionError.NOT_CONNECTED)
        }
        return try {
            currentSession?.send(message)
            Result.Success(Unit)
        }catch(err: Exception) {
            currentCoroutineContext().ensureActive()
            logger.error("Unable to send websocket message", err)
            Result.Failure(ConnectionError.MESSAGE_SEND_FAILED)
        }
    }
}