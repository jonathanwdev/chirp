package com.plcoding.chat.presentation.screens.chat_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.plcoding.chat.domain.models.ChatMessage
import com.plcoding.chat.domain.models.ChatMessageDeliveryStatus
import com.plcoding.chat.presentation.components.ChatItemHeaderRow
import com.plcoding.chat.presentation.models.ChatUi
import com.plcoding.core.designsystem.components.avatar.ChatParticipantUi
import com.plcoding.core.designsystem.theme.ChirpTheme
import com.plcoding.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Clock

@Composable
fun ChatListItemUi(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    chat: ChatUi
) {
    val isGroupChat = chat.otherParticipantUi.size > 1


    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.extended.surfaceLower
            )
            .fillMaxWidth(),

        ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ChatItemHeaderRow(
                isGroupChat = isGroupChat,
                chat = chat,
                modifier = Modifier.fillMaxWidth()
            )
            chat.lastMessage?.let {
                val previewMessage = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.extended.textSecondary
                        )
                    ) {
                        if(chat.lastMessageSenderUserName != null) {
                            append(chat.lastMessageSenderUserName + ": ")
                        }
                    }
                    append(chat.lastMessage.content)
                }
                Text(
                    text = previewMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.extended.textSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

        }
        Box(
            modifier = Modifier
                .alpha(if (isSelected) 1f else 0f)
                .background(MaterialTheme.colorScheme.primary)
                .width(4.dp)
                .fillMaxHeight()
        )
    }
}

@Composable
@Preview
private fun ChatListItemUiOnePersonPreview() {
    ChirpTheme {
        ChatListItemUi(
            isSelected = false,
            chat = ChatUi(
                id = "1",
                otherParticipantUi = listOf(
                    ChatParticipantUi("2", "Naruto Uzumaki", "NU"),
                ),
                lastMessage = ChatMessage(
                    id = "1",
                    content = "Hello, how are you?",
                    createdAt = Clock.System.now(),
                    chatId = "1",
                    senderId = "2",
                    deliveryStatus = ChatMessageDeliveryStatus.SENT
                ),
                localParticipant = ChatParticipantUi("1", "John Doe", "JD"),
                lastMessageSenderUserName = "Naruto Uzumaki"

            )
        )
    }
}

@Composable
@Preview
private fun ChatListItemUiOnePersonDarkThemePreview() {
    ChirpTheme(darkTheme = true) {
        ChatListItemUi(
            isSelected = false,
            chat = ChatUi(
                id = "1",
                otherParticipantUi = listOf(
                    ChatParticipantUi("2", "Naruto Uzumaki", "NU"),
                ),
                lastMessage = ChatMessage(
                    id = "1",
                    content = "Hello, how are you?",
                    createdAt = Clock.System.now(),
                    chatId = "1",
                    senderId = "2",
                    deliveryStatus = ChatMessageDeliveryStatus.SENT
                ),
                localParticipant = ChatParticipantUi("1", "John Doe", "JD"),
                lastMessageSenderUserName = "Naruto Uzumaki"

            )
        )
    }
}

@Composable
@Preview
private fun ChatListItemUiOnePersonSelectedPreview() {
    ChirpTheme {
        ChatListItemUi(
            isSelected = true,
            chat = ChatUi(
                id = "1",
                otherParticipantUi = listOf(
                    ChatParticipantUi("2", "Naruto Uzumaki", "NU"),
                ),
                lastMessage = ChatMessage(
                    id = "1",
                    content = "Hello, how are you?",
                    createdAt = Clock.System.now(),
                    chatId = "1",
                    senderId = "2",
                    deliveryStatus = ChatMessageDeliveryStatus.SENT
                ),
                localParticipant = ChatParticipantUi("1", "John Doe", "JD"),
                lastMessageSenderUserName = "Naruto Uzumaki"

            )
        )
    }
}

@Composable
@Preview
private fun ChatListItemUiGiantMessageSelectedPreview() {
    ChirpTheme {
        ChatListItemUi(
            isSelected = true,
            chat = ChatUi(
                id = "1",
                otherParticipantUi = listOf(
                    ChatParticipantUi("2", "Naruto Uzumaki", "NU"),
                ),
                lastMessage = ChatMessage(
                    id = "1",
                    content = "Hello, This is the new chat with a greater message for the sake of testing. Hello, This is the new chat with a greater message for the sake of testing.",
                    createdAt = Clock.System.now(),
                    chatId = "1",
                    senderId = "2",
                    deliveryStatus = ChatMessageDeliveryStatus.SENT
                ),
                localParticipant = ChatParticipantUi("1", "John Doe", "JD"),
                lastMessageSenderUserName = "Naruto Uzumaki"

            )
        )
    }
}


@Composable
@Preview
private fun ChatListItemUiPreview() {
    ChirpTheme {
        ChatListItemUi(
            isSelected = false,
            chat = ChatUi(
                id = "1",
                otherParticipantUi = listOf(
                    ChatParticipantUi("2", "Naruto Uzumaki", "NU"),
                    ChatParticipantUi("3", "Sasuke Uchiha", "SU"),
                    ChatParticipantUi("4", "Hatake Kakashi", "HK"),
                ),
                lastMessage = ChatMessage(
                    id = "1",
                    content = "Hello, how are you?",
                    createdAt = Clock.System.now(),
                    chatId = "1",
                    senderId = "3",
                    deliveryStatus = ChatMessageDeliveryStatus.SENT
                ),
                localParticipant = ChatParticipantUi("1", "John Doe", "JD"),
                lastMessageSenderUserName = "Sasuke Uchiha"

            )
        )
    }
}


@Composable
@Preview
private fun ChatListItemUiSelectedPreview() {
    ChirpTheme {
        ChatListItemUi(
            isSelected = true,
            chat = ChatUi(
                id = "1",
                otherParticipantUi = listOf(
                    ChatParticipantUi("2", "Naruto Uzumaki", "NU"),
                    ChatParticipantUi("3", "Sasuke Uchiha", "SU"),
                    ChatParticipantUi("4", "Hatake Kakashi", "HK"),
                ),
                lastMessage = ChatMessage(
                    id = "1",
                    content = "Hello, how are you?",
                    createdAt = Clock.System.now(),
                    chatId = "1",
                    senderId = "3",
                    deliveryStatus = ChatMessageDeliveryStatus.SENT
                ),
                localParticipant = ChatParticipantUi("1", "John Doe", "JD"),
                lastMessageSenderUserName = "Sasuke Uchiha"

            )
        )
    }
}



@Composable
@Preview
private fun ChatListItemUiDarkThemePreview() {
    ChirpTheme(darkTheme = true) {
        ChatListItemUi(
            isSelected = true,
            chat = ChatUi(
                id = "1",
                otherParticipantUi = listOf(
                    ChatParticipantUi("2", "Naruto Uzumaki", "NU"),
                    ChatParticipantUi("3", "Sasuke Uchiha", "SU"),
                    ChatParticipantUi("4", "Hatake Kakashi", "HK"),
                ),
                lastMessage = ChatMessage(
                    id = "1",
                    content = "Hello, how are you?",
                    createdAt = Clock.System.now(),
                    chatId = "1",
                    senderId = "3",
                    deliveryStatus = ChatMessageDeliveryStatus.SENT
                ),
                localParticipant = ChatParticipantUi("1", "John Doe", "JD"),
                lastMessageSenderUserName = "Sasuke Uchiha"

            )
        )
    }
}

