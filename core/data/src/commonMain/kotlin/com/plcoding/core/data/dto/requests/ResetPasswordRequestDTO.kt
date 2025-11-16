package com.plcoding.core.data.dto.requests

data class ResetPasswordRequestDTO(
    val password: String,
    val token: String
)
