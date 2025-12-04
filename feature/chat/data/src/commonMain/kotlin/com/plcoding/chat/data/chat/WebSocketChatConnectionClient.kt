package com.plcoding.chat.data.chat

import com.plcoding.chat.data.dto.websocket.IncomingWebSocketDTO
import com.plcoding.chat.data.dto.websocket.IncomingWebSocketType
import com.plcoding.chat.data.dto.websocket.WebSocketMessageDTO
import com.plcoding.chat.data.mappers.toDomain
import com.plcoding.chat.data.mappers.toEntity
import com.plcoding.chat.data.mappers.toNewMessage
import com.plcoding.chat.data.network.KtorWebSocketConnector
import com.plcoding.chat.database.ChirpChatDatabase
import com.plcoding.chat.domain.chat.ChatConnectionClient
import com.plcoding.chat.domain.chat.ChatRepository
import com.plcoding.chat.domain.error.ConnectionError
import com.plcoding.chat.domain.message.MessageRepository
import com.plcoding.chat.domain.models.ChatMessage
import com.plcoding.chat.domain.models.ChatMessageDeliveryStatus
import com.plcoding.core.domain.auth.SessionStorage
import com.plcoding.core.domain.util.EmptyResult
import com.plcoding.core.domain.util.onFailure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.serialization.json.Json

class WebSocketChatConnectionClient(
    private val webSocketConnector: KtorWebSocketConnector,
    private val chatRepository: ChatRepository,
    private val database: ChirpChatDatabase,
    private val sessionStorage: SessionStorage,
    private val json: Json,
    private val messageRepository: MessageRepository,
    private val applicationScope: CoroutineScope
): ChatConnectionClient {
    override val chatMessages = webSocketConnector
        .messages
        .mapNotNull {
            parseIncomingMessage(it)
        }
        .onEach { handleIncomingMessage(it) }
        .filterIsInstance<IncomingWebSocketDTO.NewMessageDTO>()
        .mapNotNull {
            database.chatMessageDao.getMessageById(it.id)?.toDomain()
        }
        .shareIn(
            applicationScope,
            SharingStarted.WhileSubscribed(5000)
        )


    override val connectionState = webSocketConnector.connectionState

    override suspend fun sendChatMessage(message: ChatMessage): EmptyResult<ConnectionError> {
        val outgoingDto = message.toNewMessage()
        val webSocketMessage = WebSocketMessageDTO(
            type = outgoingDto.type.name,
            payload = json.encodeToString(outgoingDto)
        )
        val rowJsonPayload = json.encodeToString(webSocketMessage)

        return webSocketConnector
            .sendMessage(rowJsonPayload)
            .onFailure { error ->
                messageRepository.updateMessageDeliveryStatus(
                    messageId = message.id,
                    deliveryStatus = ChatMessageDeliveryStatus.FAILED
                )
            }

    }

    private fun parseIncomingMessage(message: WebSocketMessageDTO): IncomingWebSocketDTO? {
        return when(message.type) {
            IncomingWebSocketType.NEW_MESSAGE.name ->  {
                json.decodeFromString<IncomingWebSocketDTO.NewMessageDTO>(message.payload)
            }
            IncomingWebSocketType.MESSAGE_DELETED.name ->  {
                json.decodeFromString<IncomingWebSocketDTO.MessageDeletedDTO>(message.payload)
            }
            IncomingWebSocketType.PROFILE_PICTURE_UPDATED.name ->  {
                json.decodeFromString<IncomingWebSocketDTO.ProfilePictureUpdatedDTO>(message.payload)
            }
            IncomingWebSocketType.CHAT_PARTICIPANTS_CHANGED.name ->  {
                json.decodeFromString<IncomingWebSocketDTO.ChatParticipantsChangedDTO>(message.payload)
            }
            else -> null
        }
    }

    private suspend fun handleIncomingMessage(message: IncomingWebSocketDTO) {
        when(message) {
            is IncomingWebSocketDTO.ChatParticipantsChangedDTO -> refreshChat(message)
            is IncomingWebSocketDTO.MessageDeletedDTO -> deleteMessage(message)
            is IncomingWebSocketDTO.NewMessageDTO -> handleNewMessage(message)
            is IncomingWebSocketDTO.ProfilePictureUpdatedDTO -> updateProfilePicture(message)
        }
    }

    private suspend fun refreshChat(message: IncomingWebSocketDTO.ChatParticipantsChangedDTO) {
        chatRepository.fetchChatById(message.chatId)
    }
    private suspend fun deleteMessage(message: IncomingWebSocketDTO.MessageDeletedDTO) {
        database.chatMessageDao.deleteMessageById(message.messageId)
    }
    private suspend fun handleNewMessage(message: IncomingWebSocketDTO.NewMessageDTO) {
        val chatExists = database.chatDao.getChatById(message.chatId) != null
        if(!chatExists) {
            chatRepository.fetchChatById(message.chatId)
        }

        val entity = message.toEntity()
        database.chatMessageDao.upsertMessage(entity)
    }

    private suspend fun updateProfilePicture(message: IncomingWebSocketDTO.ProfilePictureUpdatedDTO) {
        database.chatParticipantDao.updateProfilePictureUrl(
            userId = message.userId,
            newUrl = message.newUrl
        )

        val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
        if(authInfo != null) {
            sessionStorage.set(
                info = authInfo.copy(
                    user = authInfo.user.copy(
                        profilePictureUrl = message.newUrl
                    )
                )
            )
        }
    }

}