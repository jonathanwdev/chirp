package com.plcoding.chat.presentation.models

import com.plcoding.chat.domain.models.ChatMessage
import com.plcoding.core.designsystem.components.avatar.ChatParticipantUi

data class ChatUi(
    val id: String,
    val localParticipant: ChatParticipantUi,
    val otherParticipantUi: List<ChatParticipantUi>,
    val lastMessage: ChatMessage?,
    val lastMessageSenderUserName: String? = null
)
