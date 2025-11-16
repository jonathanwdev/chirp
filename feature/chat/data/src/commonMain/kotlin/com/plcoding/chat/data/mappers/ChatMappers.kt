package com.plcoding.chat.data.mappers

import com.plcoding.chat.data.dto.responses.ChatDTO
import com.plcoding.chat.database.entities.ChatEntity
import com.plcoding.chat.database.entities.ChatInfoEntity
import com.plcoding.chat.database.entities.ChatWithParticipants
import com.plcoding.chat.database.entities.MessageWithSender
import com.plcoding.chat.domain.models.Chat
import com.plcoding.chat.domain.models.ChatInfo
import com.plcoding.chat.domain.models.ChatMessage
import com.plcoding.chat.domain.models.ChatMessageDeliveryStatus
import com.plcoding.chat.domain.models.ChatParticipant
import kotlin.time.Instant

typealias DataMessageWithSender = MessageWithSender
typealias DomainMessageWithSender = com.plcoding.chat.domain.models.MessageWithSender

fun ChatDTO.toDomain(): Chat {
    return Chat(
        id = id,
        participants = participants.map { it.toDomain() },
        lastActivity = Instant.parse(lastActivityAt),
        lastMessage = lastMessage?.toDomain()
    )
}


fun ChatWithParticipants.toDomain(): Chat {
    return Chat(
        id = chat.chatId,
        participants = participants.map { it.toDomain() },
        lastActivity = Instant.fromEpochMilliseconds( chat.lastActivityAt),
        lastMessage = lastMessage?.toDomain()
    )
}


fun Chat.toEntity(): ChatEntity {
    return ChatEntity(
        chatId = id,
        lastActivityAt = lastActivity.toEpochMilliseconds(),
    )
}

fun ChatEntity.toDomain(
    participants: List<ChatParticipant>,
    lastMessage: ChatMessage? = null
): Chat {
    return Chat(
        id = chatId,
        participants = participants,
        lastActivity = Instant.fromEpochMilliseconds( lastActivityAt),
        lastMessage = lastMessage
    )
}


fun DataMessageWithSender.toDomain() : DomainMessageWithSender {
    return DomainMessageWithSender(
        message = message.toDomain(),
        sender = sender.toDomain(),
        deliveryStatus = ChatMessageDeliveryStatus.valueOf(this.message.deliveryStatus)
    )
}

fun ChatInfoEntity.toDomain(): ChatInfo {
    return ChatInfo(
        chat = chat.toDomain(
            participants = this.participants.map { it.toDomain() },
        ),
        message = messageWithSenders.map { it.toDomain() }
    )
}