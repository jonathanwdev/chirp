package com.plcoding.chat.data.mappers

import com.plcoding.chat.data.dto.responses.ChatMessageDTO
import com.plcoding.chat.data.dto.websocket.IncomingWebSocketDTO
import com.plcoding.chat.data.dto.websocket.OutgoingWebSocketDTO
import com.plcoding.chat.database.entities.ChatEntity
import com.plcoding.chat.database.entities.ChatMessageEntity
import com.plcoding.chat.database.view.LastMessageView
import com.plcoding.chat.domain.models.ChatMessage
import com.plcoding.chat.domain.models.ChatMessageDeliveryStatus
import com.plcoding.chat.domain.models.OutgoingNewMessage
import kotlin.time.Clock
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
        id = messageId,
        content = content,
        senderId = senderId,
        createdAt = Instant.fromEpochMilliseconds(timestamp),
        chatId = chatId,
        deliveryStatus = ChatMessageDeliveryStatus.valueOf(deliveryStatus)
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


fun ChatMessage.toNewMessage(): OutgoingWebSocketDTO.NewMessage {
    return OutgoingWebSocketDTO.NewMessage(
        messageId = id,
        content = content,
        chatId = chatId,
    )
}

fun IncomingWebSocketDTO.NewMessageDTO.toEntity(): ChatMessageEntity {
    return ChatMessageEntity(
        messageId = id,
        content = content,
        senderId = senderId,
        chatId = chatId,
        timestamp = Instant.parse(createdAt).toEpochMilliseconds(),
        deliveryStatus = ChatMessageDeliveryStatus.SENT.name
    )

}

fun OutgoingNewMessage.toWebSocketDTO() : OutgoingWebSocketDTO.NewMessage {
    return OutgoingWebSocketDTO.NewMessage(
        messageId = messageId,
        content = content,
        chatId = chatId,
    )
}

fun OutgoingWebSocketDTO.NewMessage.toEntity(
    senderId: String,
    deliveryStatus: ChatMessageDeliveryStatus
): ChatMessageEntity {
    return ChatMessageEntity(
        messageId = messageId,
        content = content,
        senderId = senderId,
        chatId = chatId,
        timestamp = Clock.System.now().toEpochMilliseconds(),
        deliveryStatus = deliveryStatus.name
    )
}
