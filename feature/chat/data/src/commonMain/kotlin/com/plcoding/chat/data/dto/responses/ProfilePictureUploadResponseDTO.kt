package com.plcoding.chat.data.dto.responses

import kotlinx.serialization.Serializable

@Serializable
data class ProfilePictureUploadResponseDTO(
    val uploadUrl: String,
    val publicUrl: String,
    val headers: Map<String, String>
)
