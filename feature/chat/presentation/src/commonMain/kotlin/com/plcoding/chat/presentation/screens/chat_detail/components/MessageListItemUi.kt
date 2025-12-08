package com.plcoding.chat.presentation.screens.chat_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.plcoding.chat.domain.models.ChatMessageDeliveryStatus
import com.plcoding.chat.presentation.models.MessageUi
import com.plcoding.chat.presentation.util.getChatBubbleColorForUser
import com.plcoding.core.designsystem.components.avatar.ChatParticipantUi
import com.plcoding.core.designsystem.theme.ChirpTheme
import com.plcoding.core.designsystem.theme.extended
import com.plcoding.core.presentation.util.UiText
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MessageListItemUi(
    modifier: Modifier = Modifier,
    message: MessageUi,
    messageWithOpenMenu: MessageUi.LocalUserMessage?,
    onDismissMessageMenu: () -> Unit,
    onDeleteClick: (MessageUi.LocalUserMessage) -> Unit,
    onRetryClick: (MessageUi.LocalUserMessage) -> Unit,
    onMessageLongClick: (MessageUi.LocalUserMessage) -> Unit,
) {
    Box(
        modifier = modifier,
    ) {
        when(message) {
            is MessageUi.DateSeparator -> {
                DateSeparatorUi(date = message.date.asString(), modifier = Modifier.fillMaxWidth())
            }
            is MessageUi.LocalUserMessage -> {
                LocalUserMessageUi(
                    message = message,
                    messageWithOpenMenu = messageWithOpenMenu,
                    onDeleteClick = { onDeleteClick(message) },
                    onRetryClick = { onRetryClick(message) },
                    onDismissMessageMenu = onDismissMessageMenu,
                    onMessageLongClick = { onMessageLongClick(message) },
                )
            }
            is MessageUi.OtherUserMessage -> {
                OtherUserMessageUi(
                    message = message,
                    color = getChatBubbleColorForUser(message.sender.id)
                )
            }
        }
    }
}





@Composable
private fun DateSeparatorUi(
    modifier: Modifier = Modifier,
    date: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f))
        Text(
            text = date,
            modifier = Modifier.padding(horizontal = 40.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.extended.textPlaceholder
        )
        HorizontalDivider(modifier = Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun MessageListItemUiLocalUserPreview() {
    ChirpTheme {
        MessageListItemUi(
            modifier = Modifier.fillMaxWidth().heightIn(min = 200.dp),
            message = MessageUi.LocalUserMessage(
                id = "1",
                content = "This is a message from the local user.",
                deliveryStatus = ChatMessageDeliveryStatus.SENT,
                formattedSentTime = UiText.DynamicString("10:45")
            ),
            onDismissMessageMenu = {},
            onDeleteClick = {},
            onRetryClick = {},
            onMessageLongClick = {},
            messageWithOpenMenu = null,
        )
    }
}


@Preview
@Composable
private fun MessageListItemUiLocalUserDarkPreview() {
    ChirpTheme(darkTheme = true) {
        MessageListItemUi(
            message = MessageUi.LocalUserMessage(
                id = "1",
                content = "This is a message from the local user.",
                deliveryStatus = ChatMessageDeliveryStatus.SENT,
                formattedSentTime = UiText.DynamicString("10:45")
            ),
            onDismissMessageMenu = {},
            onDeleteClick = {},
            onRetryClick = {},
            onMessageLongClick = {},
            messageWithOpenMenu = null,
        )
    }
}


@Preview
@Composable
private fun MessageListItemUiLocalUserErrorPreview() {
    ChirpTheme {
        MessageListItemUi(
            message = MessageUi.LocalUserMessage(
                id = "1",
                content = "This is a message from the local user.",
                deliveryStatus = ChatMessageDeliveryStatus.FAILED,
                formattedSentTime = UiText.DynamicString("10:45")
            ),
            onDismissMessageMenu = {},
            onDeleteClick = {},
            onRetryClick = {},
            onMessageLongClick = {},
            messageWithOpenMenu = null,
        )
    }
}

@Preview
@Composable
private fun MessageListItemUiLocalUserSendingPreview() {
    ChirpTheme {
        MessageListItemUi(
            message = MessageUi.LocalUserMessage(
                id = "1",
                content = "This is a message from the local user.",
                deliveryStatus = ChatMessageDeliveryStatus.SENDING,
                formattedSentTime = UiText.DynamicString("10:45")
            ),
            onDismissMessageMenu = {},
            onDeleteClick = {},
            onRetryClick = {},
            onMessageLongClick = {},
            messageWithOpenMenu = null,
        )
    }
}


@Preview
@Composable
private fun MessageListItemUiLocalUserMenOpenPreview() {
    ChirpTheme {
        MessageListItemUi(
            message = MessageUi.LocalUserMessage(
                id = "1",
                content = "This is a message from the local user.",
                deliveryStatus = ChatMessageDeliveryStatus.SENDING,
                formattedSentTime = UiText.DynamicString("10:45")
            ),
            onDismissMessageMenu = {},
            onDeleteClick = {},
            onRetryClick = {},
            onMessageLongClick = {},
            messageWithOpenMenu = null,
        )
    }
}

@Preview
@Composable
private fun MessageListItemUiOtherUserPreview() {
    ChirpTheme {
        MessageListItemUi(
            message = MessageUi.OtherUserMessage(
                id = "1",
                content = "This is a message from the local user.",
                formattedSentTime = UiText.DynamicString("10:45"),
                sender = ChatParticipantUi(
                    id = "1",
                    username = "John Doe",
                    initials = "JD",
                    imageUrl = null
                )
            ),
            onDismissMessageMenu = {},
            onDeleteClick = {},
            onRetryClick = {},
            onMessageLongClick = {},
            messageWithOpenMenu = null,
        )
    }
}

@Preview
@Composable
private fun MessageListItemUiOtherUserDarkPreview() {
    ChirpTheme(
        darkTheme = true
    ) {
        MessageListItemUi(
            message = MessageUi.OtherUserMessage(
                id = "1",
                content = "This is a message from the local user.",
                formattedSentTime = UiText.DynamicString("10:45"),
                sender = ChatParticipantUi(
                    id = "1",
                    username = "John Doe",
                    initials = "JD",
                    imageUrl = null
                )
            ),
            onDismissMessageMenu = {},
            onDeleteClick = {},
            onRetryClick = {},
            onMessageLongClick = {},
            messageWithOpenMenu = null,
        )
    }
}


