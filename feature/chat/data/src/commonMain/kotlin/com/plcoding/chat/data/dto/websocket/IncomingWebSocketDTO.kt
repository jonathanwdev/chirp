package com.plcoding.chat.data.dto.websocket

import kotlinx.serialization.Serializable

enum class IncomingWebSocketType {
    NEW_MESSAGE,
    MESSAGE_DELETED,
    PROFILE_PICTURE_UPDATED,
    CHAT_PARTICIPANTS_CHANGED,
}

@Serializable
sealed class IncomingWebSocketDTO(
    private val type: IncomingWebSocketType
) {
    @Serializable
    data class NewMessageDTO(
        val id: String,
        val chatId: String,
        val content: String,
        val senderId: String,
        val createdAt: String
    ) : IncomingWebSocketDTO(IncomingWebSocketType.NEW_MESSAGE)

    @Serializable
    data class MessageDeletedDTO(
        val messageId: String,
        val chatId: String,
    ) : IncomingWebSocketDTO(IncomingWebSocketType.MESSAGE_DELETED)

    @Serializable
    data class ProfilePictureUpdatedDTO(
        val userId: String,
        val newUrl: String,
    ) : IncomingWebSocketDTO(IncomingWebSocketType.PROFILE_PICTURE_UPDATED)

    @Serializable
    data class ChatParticipantsChangedDTO(
        val chatId: String
    ) : IncomingWebSocketDTO(IncomingWebSocketType.CHAT_PARTICIPANTS_CHANGED)
}