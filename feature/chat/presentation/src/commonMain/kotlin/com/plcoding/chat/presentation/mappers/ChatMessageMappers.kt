package com.plcoding.chat.presentation.mappers

import com.plcoding.chat.domain.models.MessageWithSender
import com.plcoding.chat.presentation.models.MessageUi
import com.plcoding.chat.presentation.util.DateUtils
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


fun List<MessageWithSender>.toUiList( localUserId: String): List<MessageUi> {
    return this
        .sortedByDescending { it.message.createdAt }
        .groupBy {
            it.message.createdAt.toLocalDateTime(TimeZone.currentSystemDefault()).date
        }
        .flatMap { (date, messages) ->
            messages.map { it.toUi(localUserId) } + MessageUi.DateSeparator(
                id = date.toString(),
                date = DateUtils.formateDateSeparator(date)
            )
        }

}

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