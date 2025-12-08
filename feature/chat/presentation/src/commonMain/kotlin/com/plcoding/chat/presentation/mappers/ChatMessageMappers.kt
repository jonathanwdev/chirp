package com.plcoding.chat.presentation.mappers

import com.plcoding.chat.domain.models.MessageWithSender
import com.plcoding.chat.presentation.models.MessageUi
import com.plcoding.chat.presentation.util.DateUtils

fun MessageWithSender.toUi(
    localUserId: String,
): MessageUi {
    val isMine = this.sender.userId == localUserId
    return if(isMine) {
        MessageUi.LocalUserMessage(
            id = message.id,
            content = message.content,
            deliveryStatus = message.deliveryStatus,
            formattedSentTime = DateUtils.formatMessageTime(instant = message.createdAt)
        )
    } else {
        MessageUi.OtherUserMessage(
            id = message.id,
            content = message.content,
            formattedSentTime = DateUtils.formatMessageTime(instant = message.createdAt),
            sender = sender.toUi()
        )
    }
}