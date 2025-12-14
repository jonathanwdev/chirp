package com.plcoding.chat.data.dto.requests

import kotlinx.serialization.Serializable


@Serializable
data class ConfirmProfilePictureRequestDTO(
    val publicUrl: String,
)
