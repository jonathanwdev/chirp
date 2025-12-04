package com.plcoding.chat.data.dto.websocket

import kotlinx.serialization.Serializable

enum class OutgoingWebSocketType {
    NEW_MESSAGE,
}


@Serializable
sealed class OutgoingWebSocketDTO(
    val type: OutgoingWebSocketType
) {
    @Serializable
    data class NewMessage(
        val chatId: String,
        val messageId: String,
        val content: String,
    ): OutgoingWebSocketDTO(OutgoingWebSocketType.NEW_MESSAGE)
}
