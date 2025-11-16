package com.plcoding.chat.presentation.screens.chat_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.plcoding.chat.presentation.models.MessageUi
import com.plcoding.core.designsystem.components.avatar.ChirpAvatarPhoto
import com.plcoding.core.designsystem.components.chat.ChatBubblePosition
import com.plcoding.core.designsystem.components.chat.ChirpChatBubble

@Composable
fun OtherUserMessageUi(
    modifier: Modifier = Modifier,
    message: MessageUi.OtherUserMessage,
    color: Color
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ChirpAvatarPhoto(
            displayText = message.sender.initials,
            imageUrl = message.sender.imageUrl
        )
        ChirpChatBubble(
            messageContent = message.content,
            formattedDateTime = message.formattedSentTime.asString(),
            trianglePosition = ChatBubblePosition.LEFT,
            sender = message.sender.username,
            color = color
        )
    }
}