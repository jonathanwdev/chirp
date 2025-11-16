package com.plcoding.chat.data.dto.responses

import kotlinx.serialization.Serializable


@Serializable
data class ChatMessageDTO(
    val id: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val createdAt: String
)
