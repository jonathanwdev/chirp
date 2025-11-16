package com.plcoding.chat.presentation.mappers

import com.plcoding.chat.domain.models.Chat
import com.plcoding.chat.presentation.models.ChatUi

fun Chat.toUi(localParticipantId: String): ChatUi {
    val (local, other) = participants.partition { it.userId == localParticipantId }

    return ChatUi(
        id = id,
        localParticipant = local.first().toUi(),
        otherParticipantUi = other.map { it.toUi() },
        lastMessage = lastMessage,
        lastMessageSenderUserName = participants
            .find { it.userId == lastMessage?.senderId }
            ?.username
    )
}