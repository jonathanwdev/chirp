package com.plcoding.chat.data.dto.requests

import io.ktor.util.Platform
import kotlinx.serialization.Serializable


@Serializable
data class RegisterDeviceTokenRequestDTO(
    val token: String,
    val platform: String
)
