package com.plcoding.chat.data.dto.requests

import kotlinx.serialization.Serializable


@Serializable
data class ParticipantsRequest(
    val userIds: List<String>
)
