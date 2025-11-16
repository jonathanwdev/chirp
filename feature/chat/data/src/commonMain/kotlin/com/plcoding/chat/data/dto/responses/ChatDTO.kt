package com.plcoding.chat.data.dto.responses

import kotlinx.serialization.Serializable


@Serializable
data class ChatDTO(
    val id: String,
    val participants: List<ChatParticipantResponseDTO>,
    val lastActivityAt: String,
    val lastMessage: ChatMessageDTO?
)
