package com.plcoding.core.data.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDTO(
    val username: String,
    val email: String,
    val password: String

)
