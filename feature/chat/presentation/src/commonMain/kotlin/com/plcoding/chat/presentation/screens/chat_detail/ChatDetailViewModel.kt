package com.plcoding.chat.presentation.screens.chat_detail

import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.chat.domain.chat.ChatConnectionClient
import com.plcoding.chat.domain.chat.ChatRepository
import com.plcoding.chat.domain.message.MessageRepository
import com.plcoding.chat.domain.models.ChatMessage
import com.plcoding.chat.domain.models.ConnectionState
import com.plcoding.chat.domain.models.OutgoingNewMessage
import com.plcoding.chat.presentation.mappers.toUi
import com.plcoding.chat.presentation.models.MessageUi
import com.plcoding.core.domain.auth.SessionStorage
import com.plcoding.core.domain.util.DataErrorException
import com.plcoding.core.domain.util.Paginator
import com.plcoding.core.domain.util.onFailure
import com.plcoding.core.domain.util.onSuccess
import com.plcoding.core.presentation.util.toUiText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)
class ChatDetailViewModel(
    private val chatRepository: ChatRepository,
    private val sessionStorage: SessionStorage,
    private val messageRepository: MessageRepository,
    private val connectionClient: ChatConnectionClient,
) : ViewModel() {
    private val eventChannel = Channel<ChatDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _chatId = MutableStateFlow<String?>(null)

    private var hasLoadedInitialData = false
    private var currentPaginator: Paginator<String?, ChatMessage>? = null

    private val chatInfoFlow = _chatId
        .onEach { chatId ->
            if(chatId != null) {
                setPaginatorForChat(chatId)
            } else {
                null
            }
        }
        .flatMapLatest { chatId ->
            if(chatId != null) {
                chatRepository.getChatInfoById(chatId)
            } else emptyFlow()
        }
    private val _state = MutableStateFlow(ChatDetailState())

    private val canSendMessage = snapshotFlow { _state.value.messageTextFieldState.text.toString()  }
        .map { it.isBlank() }
        .combine(connectionClient.connectionState) { isMessageBlank, connectionState ->
            !isMessageBlank && connectionState == ConnectionState.CONNECTED
        }

    private val stateWithMessages = combine(
        _state,
        chatInfoFlow,
        sessionStorage.observeAuthInfo(),
    ) { currentState, chatInfo, sessionInfo ->
        if(sessionInfo == null) {
            return@combine ChatDetailState()
        }
        currentState.copy(
            chatUi = chatInfo.chat.toUi(sessionInfo.user.id),
            messages = chatInfo.message.map { it.toUi(sessionInfo.user.id) }
        )
    }

    val state = _chatId
        .flatMapLatest { chatId ->
            if(chatId != null) {
                stateWithMessages
            }else  {
                _state
            }
        }
        .onStart {
            if (!hasLoadedInitialData) {
                observeConnectionState()
                observerChatMessages()
                observeCanSendMessage()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ChatDetailState()
        )

    fun onAction(action: ChatDetailAction) {
        when (action) {
            is ChatDetailAction.OnSelectChat -> switchChat(action.chatId)
            ChatDetailAction.OnBackClick -> {}
            ChatDetailAction.OnChatMembersClick -> {}
            ChatDetailAction.OnChatOptionsClick -> onChatOptionsClick()
            is ChatDetailAction.OnDeleteMessageClick -> deleteMessage(action.message)
            ChatDetailAction.OnDismissChatOptions -> onDismissChatOptions()
            ChatDetailAction.OnDismissMessageMenu -> onDismissMessageMenu()
            ChatDetailAction.OnLeaveChatClick -> onLeaveChatClick()
            is ChatDetailAction.OnMessageLongClick -> onMessageLongClick(action.message)
            is ChatDetailAction.OnRetryClick -> retryMessage(action.message)
            ChatDetailAction.OnScrollToTop -> {}
            ChatDetailAction.OnSendMessageClick -> sendMessage()
            else -> Unit
        }
    }

    private fun onMessageLongClick(message: MessageUi.LocalUserMessage) {
        _state.update { it.copy(
            messageWithOpenMenu = message
        ) }
    }

    private fun onDismissMessageMenu() {
        _state.update { it.copy(
            messageWithOpenMenu = null
        ) }
    }

    private fun deleteMessage(message: MessageUi.LocalUserMessage) {
        viewModelScope.launch {
            messageRepository
                .deleteMessage(message.id)
                .onFailure { error ->
                    eventChannel.send(ChatDetailEvent.OnError(error.toUiText()))
                }
        }
    }

    private fun retryMessage(message: MessageUi.LocalUserMessage) {
        viewModelScope.launch {
            messageRepository
                .retryMessage(message.id)
                .onFailure { failure ->
                    eventChannel.send(ChatDetailEvent.OnError(failure.toUiText()))
                }
        }
    }

    private fun sendMessage() {
        val currentChatId = _chatId.value
        val content = state.value.messageTextFieldState.text.toString().trim()
        if(content.isBlank() || currentChatId == null) return
        viewModelScope.launch {
            val message = OutgoingNewMessage(
                chatId = currentChatId,
                content = content,
                messageId = Uuid.random().toString()
            )
            messageRepository
                .sendMessage(message)
                .onSuccess {
                    state.value.messageTextFieldState.clearText()
                }
                .onFailure { error ->
                    eventChannel.send(ChatDetailEvent.OnError(error.toUiText()))
                }

        }
    }

    private fun observeCanSendMessage() {
        canSendMessage.onEach { canSend ->
            _state.update { it.copy(canSendMessage = canSend) }
        }.launchIn(viewModelScope)
    }
    private fun observerChatMessages() {
        val currentMessages = state
            .map { it.messages }
            .distinctUntilChanged()

        val newMessages = _chatId
            .flatMapLatest { chatId ->
                if(chatId != null) {
                    messageRepository
                        .getMessagesForChat(chatId)
                } else emptyFlow()
            }

        val isNearBottom = state.map { it.isNearBottom }.distinctUntilChanged()
        combine(
            currentMessages,
            newMessages,
            isNearBottom
        ) { currentMessages, newMessages, isNearBottom ->
            val lasNewId = newMessages.lastOrNull()?.message?.id
            val currentId = currentMessages.lastOrNull()?.id
            if(lasNewId != currentId) {
                eventChannel.send(ChatDetailEvent.OnNewMessage)
            }
        }.launchIn(viewModelScope)
    }

    private fun observeConnectionState() {
        connectionClient
            .connectionState
            .onEach { connectionState ->
                if(connectionState == ConnectionState.CONNECTED) {
                    _chatId.value?.let {
                        messageRepository.fetchMessages(it, before = null)
                    }
                }
                _state.update {
                    it.copy(
                        connectionState = connectionState
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun setPaginatorForChat(chatId: String) {
        currentPaginator = Paginator(
            initialKey = null,
            onLoadUpdated = { isLoading ->
                _state.update { it.copy(isPaginationLoading = isLoading) }
            },
            onRequest = { beforeTimeStamp ->
                messageRepository.fetchMessages(chatId, beforeTimeStamp)
            },
            getNextKey = { messages ->
                messages.minOfOrNull { it.createdAt }?.toString()
            },
            onError = { throwable ->
                if(throwable is DataErrorException) {
                    eventChannel.send(ChatDetailEvent.OnError(throwable.error.toUiText()))
                }
            },
            onSuccess = { messages, _ ->
                _state.update { it.copy(
                    endReached = messages.isEmpty(),

                ) }
            }

        )
        _state.update { it.copy(
            endReached = false,
            isPaginationLoading = false
        ) }
        viewModelScope.launch {
            currentPaginator?.loadNextItems()
        }
    }

    private fun onLeaveChatClick() {
        val chatId = _chatId.value ?: return
        _state.update {
            it.copy(isChatOptionsOpen = false)
        }
        viewModelScope.launch {
            chatRepository
                .leaveChat(chatId)
                .onSuccess {
                    _state.value.messageTextFieldState.clearText()

                    _chatId.update { null }
                    _state.update { it.copy(
                        chatUi = null,
                        messages = emptyList(),
                        bannerState = BannerState()
                    ) }
                }
                .onFailure { error ->
                    eventChannel.send(ChatDetailEvent.OnError(error = error.toUiText()))
                }
        }
    }

    private fun onDismissChatOptions() {
        _state.update {
            it.copy(isChatOptionsOpen = false)
        }
    }

    private fun onChatOptionsClick() {
        _state.update {
            it.copy(isChatOptionsOpen = true)
        }
    }

    private fun switchChat(chatId: String?) {
        _chatId.update {  chatId }
        viewModelScope.launch {
            chatId?.let {
                chatRepository
                    .fetchChatById(chatId)

            }
        }
    }


}