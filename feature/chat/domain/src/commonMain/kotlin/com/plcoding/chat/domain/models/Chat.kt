package com.plcoding.chat.domain.models

import kotlin.time.Instant

data class Chat(
    val id: String,
    val participants: List<ChatParticipant>,
    val lastActivity: Instant,
    val lastMessage: ChatMessage?
)
