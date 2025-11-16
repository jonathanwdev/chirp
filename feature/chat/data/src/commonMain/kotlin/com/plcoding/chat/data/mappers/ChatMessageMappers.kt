package com.plcoding.chat.data.mappers

import com.plcoding.chat.data.dto.responses.ChatMessageDTO
import com.plcoding.chat.database.entities.ChatEntity
import com.plcoding.chat.database.entities.ChatMessageEntity
import com.plcoding.chat.database.view.LastMessageView
import com.plcoding.chat.domain.models.ChatMessage
import com.plcoding.chat.domain.models.ChatMessageDeliveryStatus
import kotlin.time.Instant

fun ChatMessageDTO.toDomain(): ChatMessage {
    return ChatMessage(
        id = id,
        content = content,
        senderId = senderId,
        createdAt = Instant.parse(createdAt),
        chatId = chatId,
        deliveryStatus = ChatMessageDeliveryStatus.SENT
    )
}

fun ChatMessageEntity.toDomain(): ChatMessage {
    return ChatMessage(
        id = chatId,
        content = content,
        senderId = senderId,
        createdAt = Instant.fromEpochMilliseconds(timestamp),
        chatId = chatId,
        deliveryStatus = ChatMessageDeliveryStatus.SENT
    )
}


fun LastMessageView.toDomain(): ChatMessage {
    return ChatMessage(
        id = messageId,
        content = content,
        senderId = senderId,
        createdAt = Instant.fromEpochMilliseconds(timestamp),
        chatId = chatId,
        deliveryStatus = ChatMessageDeliveryStatus.valueOf(this.deliveryStatus)
    )
}

fun ChatMessage.toEntity(): ChatMessageEntity {
    return ChatMessageEntity(
        messageId = id,
        content = content,
        senderId = senderId,
        chatId = chatId,
        timestamp = createdAt.toEpochMilliseconds(),
        deliveryStatus = deliveryStatus.name
    )
}

fun ChatMessage.toLastMessageView(): LastMessageView {
    return LastMessageView(
        messageId = id,
        content = content,
        senderId = senderId,
        chatId = chatId,
        timestamp = createdAt.toEpochMilliseconds(),
        deliveryStatus = deliveryStatus.name
    )
}

