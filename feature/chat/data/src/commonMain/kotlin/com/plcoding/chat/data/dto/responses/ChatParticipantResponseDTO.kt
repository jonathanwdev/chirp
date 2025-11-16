package com.plcoding.chat.data.dto.responses

import kotlinx.serialization.Serializable

@Serializable
data class ChatParticipantResponseDTO(
    val userId: String,
    val username: String,
    val profilePictureUrl: String? = null
)