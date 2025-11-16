package com.plcoding.chat.domain.models

data class ChatInfo(
    val chat: Chat,
    val message: List<MessageWithSender>
)
