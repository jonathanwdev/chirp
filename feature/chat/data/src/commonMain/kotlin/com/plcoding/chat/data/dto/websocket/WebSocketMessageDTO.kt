package com.plcoding.chat.data.dto.websocket

import kotlinx.serialization.Serializable


@Serializable
data class WebSocketMessageDTO(
    val type: String,
    val payload: String
)
