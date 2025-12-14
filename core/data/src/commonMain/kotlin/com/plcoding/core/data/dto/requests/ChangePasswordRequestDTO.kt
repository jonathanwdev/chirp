package com.plcoding.core.data.dto.requests

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequestDTO(
    val oldPassword: String,
    val newPassword: String
)
