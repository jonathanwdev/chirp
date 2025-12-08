package com.plcoding.chat.data.dto.websocket

import kotlinx.serialization.Serializable

enum class IncomingWebSocketType {
    NEW_MESSAGE,
    MESSAGE_DELETED,
    PROFILE_PICTURE_UPDATED,
    CHAT_PARTICIPANTS_CHANGED,
}

@Serializable
sealed interface IncomingWebSocketDTO {
    @Serializable
    data class NewMessageDTO(
        val id: String,
        val chatId: String,
        val content: String,
        val senderId: String,
        val createdAt: String,
        val type: IncomingWebSocketType = IncomingWebSocketType.NEW_MESSAGE
    ) : IncomingWebSocketDTO

    @Serializable
    data class MessageDeletedDTO(
        val messageId: String,
        val chatId: String,
        val type: IncomingWebSocketType = IncomingWebSocketType.MESSAGE_DELETED
    ) : IncomingWebSocketDTO

    @Serializable
    data class ProfilePictureUpdatedDTO(
        val userId: String,
        val newUrl: String,
        val type: IncomingWebSocketType = IncomingWebSocketType.PROFILE_PICTURE_UPDATED
    ) : IncomingWebSocketDTO

    @Serializable
    data class ChatParticipantsChangedDTO(
        val chatId: String,
        val type: IncomingWebSocketType = IncomingWebSocketType.CHAT_PARTICIPANTS_CHANGED
    ) : IncomingWebSocketDTO
}