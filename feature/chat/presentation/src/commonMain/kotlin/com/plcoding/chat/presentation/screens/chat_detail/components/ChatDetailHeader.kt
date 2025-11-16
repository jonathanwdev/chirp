package com.plcoding.chat.presentation.screens.chat_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chirp.core.designsystem.generated.resources.Res as DesignSystemRes
import chirp.core.designsystem.generated.resources.arrow_left_icon
import chirp.core.designsystem.generated.resources.dots_icon
import chirp.core.designsystem.generated.resources.log_out_icon
import chirp.core.designsystem.generated.resources.users_icon
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.chat_members
import chirp.feature.chat.presentation.generated.resources.go_back
import chirp.feature.chat.presentation.generated.resources.leave_chat
import chirp.feature.chat.presentation.generated.resources.open_chat_options_menu
import com.plcoding.chat.domain.models.ChatMessageDeliveryStatus
import com.plcoding.chat.presentation.components.ChatItemHeaderRow
import com.plcoding.chat.presentation.models.ChatUi
import com.plcoding.core.designsystem.components.buttons.ChirpIconButton
import com.plcoding.core.designsystem.components.dropdown.ChirpDropDownMenu
import com.plcoding.core.designsystem.components.dropdown.DropDownItem
import com.plcoding.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.plcoding.core.designsystem.components.avatar.ChatParticipantUi
import com.plcoding.core.designsystem.theme.ChirpTheme
import kotlin.time.Instant

@Composable
fun ChatDetailHeader(
    modifier: Modifier = Modifier,
    chatUi: ChatUi?,
    isDetailPresent: Boolean,
    isChatOptionsDropDownOpen: Boolean,
    onChatOptionsClick: () -> Unit,
    onDismissChatOptions: () -> Unit,
    onManageChatClick: () -> Unit,
    onBackClick: () -> Unit,
    onLeaveChatClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (!isDetailPresent) {
            ChirpIconButton(
                onClick = onBackClick,
            ) {
                Icon(
                    imageVector = vectorResource(DesignSystemRes.drawable.arrow_left_icon),
                    contentDescription = stringResource(Res.string.go_back),
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.extended.textSecondary
                )
            }
        }
        if(chatUi != null) {
            val isGroupChat = chatUi.otherParticipantUi.size > 1

            ChatItemHeaderRow(
                chat = chatUi,
                isGroupChat = isGroupChat,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onManageChatClick()
                    }
            )
        }else {
            Spacer(Modifier.weight(1f))
        }


        Box {
            ChirpIconButton(
                onClick = onChatOptionsClick
            ) {
                Icon(
                    imageVector = vectorResource(DesignSystemRes.drawable.dots_icon),
                    contentDescription = stringResource(Res.string.open_chat_options_menu),
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.extended.textSecondary
                )
            }
            ChirpDropDownMenu(
                isOpen = isChatOptionsDropDownOpen,
                onDismiss = onDismissChatOptions,
                items = listOf(
                    DropDownItem(
                        title = stringResource(Res.string.chat_members),
                        icon = vectorResource(DesignSystemRes.drawable.users_icon),
                        contentColor = MaterialTheme.colorScheme.extended.textSecondary,
                        onClick = onManageChatClick
                    ),
                    DropDownItem(
                        title = stringResource(Res.string.leave_chat),
                        icon = vectorResource(DesignSystemRes.drawable.log_out_icon),
                        contentColor = MaterialTheme.colorScheme.extended.destructiveHover,
                        onClick = onLeaveChatClick
                    ),
                ),
            )
        }
    }

}

@Preview
@Composable
fun ChatDetailHeaderPreview() {
    ChirpTheme {

        ChatDetailHeader(
            chatUi = ChatUi(
                id = "chat1",
                localParticipant = ChatParticipantUi(
                    id = "1",
                    username = "Local User",
                    initials = "LU"
                ),
                otherParticipantUi = listOf(
                    ChatParticipantUi(
                        id = "2",
                        username = "Other User",
                        initials = "OU"
                    )
                ),
                lastMessage = com.plcoding.chat.domain.models.ChatMessage(
                    id = "msg1",
                    chatId = "chat1",
                    content = "Hello there!",
                    createdAt = Instant.DISTANT_PAST,
                    senderId = "2",
                    deliveryStatus = ChatMessageDeliveryStatus.SENT
                ),
                lastMessageSenderUserName = "Other User"
            ),
            isDetailPresent = false,
            isChatOptionsDropDownOpen = false,
            onChatOptionsClick = {},
            onDismissChatOptions = {},
            onManageChatClick = {},
            onBackClick = {},
            onLeaveChatClick = {}
        )
    }

}

@Preview
@Composable
fun ChatDetailHeaderGroupChatPreview() {
    ChirpTheme {
        ChatDetailHeader(
            chatUi = ChatUi(
                id = "chat1",
                localParticipant = ChatParticipantUi(
                    id = "1",
                    username = "Local User",
                    initials = "LU"
                ),
                otherParticipantUi = listOf(
                    ChatParticipantUi(
                        id = "2",
                        username = "Johny Depp",
                        initials = "JD"
                    ),
                    ChatParticipantUi(
                        id = "3",
                        username = "Naruto Uzumaki",
                        initials = "NU"
                    ),
                    ChatParticipantUi(
                        id = "4",
                        username = "John Doe",
                        initials = "JD"
                    ),
                ),
                lastMessage = com.plcoding.chat.domain.models.ChatMessage(
                    id = "msg1",
                    chatId = "chat1",
                    content = "Hello there!",
                    createdAt = Instant.DISTANT_PAST,
                    senderId = "2",
                    deliveryStatus = ChatMessageDeliveryStatus.SENT
                ),
                lastMessageSenderUserName = "Other User"
            ),
            isDetailPresent = false,
            isChatOptionsDropDownOpen = false,
            onChatOptionsClick = {},
            onDismissChatOptions = {},
            onManageChatClick = {},
            onBackClick = {},
            onLeaveChatClick = {}
        )
    }
}

@Preview
@Composable
fun ChatDetailHeaderMenuOpenPreview() {
    ChirpTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            ChatDetailHeader(
                chatUi = ChatUi(
                    id = "chat1",
                    localParticipant = ChatParticipantUi(
                        id = "1",
                        username = "Local User",
                        initials = "LU"
                    ),
                    otherParticipantUi = listOf(
                        ChatParticipantUi(
                            id = "2",
                            username = "Other User",
                            initials = "OU"
                        )
                    ),
                    lastMessage = com.plcoding.chat.domain.models.ChatMessage(
                        id = "msg1",
                        chatId = "chat1",
                        content = "Hello there!",
                        createdAt = Instant.DISTANT_PAST,
                        senderId = "2",
                        deliveryStatus = ChatMessageDeliveryStatus.SENT
                    ),
                    lastMessageSenderUserName = "Other User"
                ),
                isDetailPresent = false,
                isChatOptionsDropDownOpen = true,
                onChatOptionsClick = {},
                onDismissChatOptions = {},
                onManageChatClick = {},
                onBackClick = {},
                onLeaveChatClick = {}
            )
        }
    }
}