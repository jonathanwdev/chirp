package com.plcoding.chat.data.message

import com.plcoding.chat.data.database.safeDatabaseUpdate
import com.plcoding.chat.data.dto.websocket.OutgoingWebSocketDTO
import com.plcoding.chat.data.dto.websocket.WebSocketMessageDTO
import com.plcoding.chat.data.mappers.toDomain
import com.plcoding.chat.data.mappers.toEntity
import com.plcoding.chat.data.mappers.toNewMessage
import com.plcoding.chat.data.mappers.toWebSocketDTO
import com.plcoding.chat.data.network.KtorWebSocketConnector
import com.plcoding.chat.database.ChirpChatDatabase
import com.plcoding.chat.domain.message.ChatMessageService
import com.plcoding.chat.domain.message.MessageRepository
import com.plcoding.chat.domain.models.ChatMessage
import com.plcoding.chat.domain.models.ChatMessageDeliveryStatus
import com.plcoding.chat.domain.models.MessageWithSender
import com.plcoding.chat.domain.models.OutgoingNewMessage
import com.plcoding.core.domain.auth.SessionStorage
import com.plcoding.core.domain.util.DataError
import com.plcoding.core.domain.util.EmptyResult
import com.plcoding.core.domain.util.Result
import com.plcoding.core.domain.util.onFailure
import com.plcoding.core.domain.util.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.time.Clock

class OfflineFirstMessageRepository(
    private val database: ChirpChatDatabase,
    private val chatMessageService: ChatMessageService,
    private val sessionStorage: SessionStorage,
    private val json: Json,
    private val webSocketConnector: KtorWebSocketConnector,
    private val applicationScope: CoroutineScope
) : MessageRepository {
    override suspend fun updateMessageDeliveryStatus(
        messageId: String,
        deliveryStatus: ChatMessageDeliveryStatus
    ): EmptyResult<DataError.Local> {
        return safeDatabaseUpdate {
            database.chatMessageDao.updateDeliveryStatusMessage(
                messageId = messageId,
                deliveryStatus = deliveryStatus.name,
                timestamp = Clock.System.now().toEpochMilliseconds()
            )
        }
    }

    override suspend fun fetchMessages(
        chatId: String,
        before: String?
    ): Result<List<ChatMessage>, DataError> {
        return chatMessageService
            .fetchMessages(chatId, before)
            .onSuccess { messages ->
                return safeDatabaseUpdate {
                    database.chatMessageDao.upsertMessagesAndSyncIfNecessary(
                        chatId = chatId,
                        serverMessages = messages.map { it.toEntity() },
                        pageSize = ChatMessageConstants.PAGE_SIZE,
                        shouldSync = before == null
                    )
                    messages
                }
            }
    }

    override suspend fun sendMessage(message: OutgoingNewMessage): EmptyResult<DataError> {
        return safeDatabaseUpdate {
            val dto = message.toWebSocketDTO()
            val localUser = sessionStorage.observeAuthInfo().first()?.user ?: return Result.Failure(
                DataError.Local.NOT_FOUND
            )
            val entity = dto.toEntity(
                senderId = localUser.id,
                deliveryStatus = ChatMessageDeliveryStatus.SENDING
            )
            database.chatMessageDao.upsertMessage(entity)
            return webSocketConnector
                .sendMessage(
                    dto.toJsonPayload()
                )
                .onFailure {
                    applicationScope.launch {
                        database.chatMessageDao.updateDeliveryStatusMessage(
                            messageId = entity.messageId,
                            timestamp = Clock.System.now().toEpochMilliseconds(),
                            deliveryStatus = ChatMessageDeliveryStatus.FAILED.name
                        )
                    }.join()

                }
        }
    }

    override suspend fun retryMessage(messageId: String): EmptyResult<DataError> {
        return safeDatabaseUpdate {
            val message = database.chatMessageDao.getMessageById(messageId) ?: return Result.Failure(
                DataError.Local.NOT_FOUND)
            database.chatMessageDao.updateDeliveryStatusMessage(
                messageId = messageId,
                timestamp = Clock.System.now().toEpochMilliseconds(),
                deliveryStatus = ChatMessageDeliveryStatus.SENDING.name
            )
            val outgoingNewMessage = OutgoingWebSocketDTO.NewMessage(
               chatId = message.chatId,
                messageId = messageId,
                content = message.content
            )
            return webSocketConnector
                .sendMessage(outgoingNewMessage.toJsonPayload())
                .onFailure {
                    applicationScope.launch {
                        database.chatMessageDao.upsertMessage(
                            message.copy(
                                deliveryStatus = ChatMessageDeliveryStatus.FAILED.name,
                                timestamp = Clock.System.now().toEpochMilliseconds(),
                            )
                        )
                    }.join()
                }
        }
    }

    override suspend fun deleteMessage(messageId: String): EmptyResult<DataError.Remote> {
        return chatMessageService
            .deleteMessage(messageId)
            .onSuccess {
                applicationScope.launch {
                    database.chatMessageDao.deleteMessageById(messageId)
                }.join()
            }
    }

    override fun getMessagesForChat(chatId: String): Flow<List<MessageWithSender>> {
        return database.chatMessageDao
            .getMessagesByChatId(chatId)
            .map { messages -> messages.map { it.toDomain() } }
    }

    private fun OutgoingWebSocketDTO.NewMessage.toJsonPayload(): String {
        val webSocketMessage = WebSocketMessageDTO(
            type = this.type.name,
            payload = json.encodeToString(this)
        )
        return json.encodeToString(webSocketMessage)
    }
}