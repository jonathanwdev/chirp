package com.plcoding.chat.presentation.screens.chat_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.no_chat_selected
import chirp.feature.chat.presentation.generated.resources.select_a_chat
import com.plcoding.chat.domain.models.ChatMessage
import com.plcoding.chat.domain.models.ChatMessageDeliveryStatus
import com.plcoding.chat.presentation.components.ChatHeader
import com.plcoding.chat.presentation.components.EmptySection
import com.plcoding.chat.presentation.models.ChatUi
import com.plcoding.chat.presentation.models.MessageUi
import com.plcoding.chat.presentation.screens.chat_detail.components.ChatDetailHeader
import com.plcoding.chat.presentation.screens.chat_detail.components.MessageBox
import com.plcoding.chat.presentation.screens.chat_detail.components.MessageList
import com.plcoding.core.designsystem.components.avatar.ChatParticipantUi
import com.plcoding.core.designsystem.theme.ChirpTheme
import com.plcoding.core.designsystem.theme.extended
import com.plcoding.core.presentation.util.ObserveAsEvent
import com.plcoding.core.presentation.util.UiText
import com.plcoding.core.presentation.util.clearFocusOnTap
import com.plcoding.core.presentation.util.currentDeviceConfiguration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatDetailRoot(
    chatId: String?,
    isDetailPresent: Boolean,
    onBack: () -> Unit,
    onChatMembersClick: () -> Unit,
    viewModel: ChatDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarState = remember { SnackbarHostState() }

    ObserveAsEvent(viewModel.events) {event ->
        when(event) {
            ChatDetailEvent.OnChatLeft -> {
                onBack()
            }
            ChatDetailEvent.OnNewMessage -> {
                Unit
            }
            is ChatDetailEvent.OnError -> {
                snackBarState.showSnackbar(event.error.asStringAsync())
            }
        }
    }

    LaunchedEffect(chatId) {
        viewModel.onAction(ChatDetailAction.OnSelectChat(chatId))
    }

    val scope = rememberCoroutineScope()
    BackHandler(
        enabled = !isDetailPresent
    ) {
        scope.launch {
            delay(300)
            viewModel.onAction(ChatDetailAction.OnSelectChat(null))
        }
        onBack()
    }

    ChatDetailScreen(
        state = state,
        isDetailPresent = isDetailPresent,
        snackBarState = snackBarState,
        onAction =  { action ->
            when(action) {
                is ChatDetailAction.OnChatMembersClick -> onChatMembersClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
fun ChatDetailScreen(
    state: ChatDetailState,
    isDetailPresent: Boolean,
    snackBarState: SnackbarHostState,
    onAction: (ChatDetailAction) -> Unit,
) {
    val configuration = currentDeviceConfiguration()
    val messageListState = rememberLazyListState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = {
            SnackbarHost(snackBarState)
        },
        containerColor = if (!configuration.isWideScreen) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.extended.surfaceLower
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .clearFocusOnTap()
                .padding(innerPadding)
                .then(
                    if (configuration.isWideScreen) {
                        Modifier.padding(horizontal = 8.dp)
                    } else Modifier
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DynamicRoundedCornerColumn(
                    isCornersRounded = configuration.isWideScreen,
                    modifier = Modifier.weight(1f).fillMaxWidth()
                ) {
                    if(state.chatUi == null) {
                        EmptySection(
                            title = stringResource(Res.string.no_chat_selected),
                            description = stringResource(Res.string.select_a_chat),
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {

                        ChatHeader {
                            ChatDetailHeader(
                                modifier = Modifier.fillMaxWidth(),
                                chatUi = state.chatUi,
                                isDetailPresent = isDetailPresent,
                                isChatOptionsDropDownOpen = state.isChatOptionsOpen,
                                onChatOptionsClick = {
                                    onAction(ChatDetailAction.OnChatOptionsClick)
                                },
                                onDismissChatOptions = {
                                    onAction(ChatDetailAction.OnDismissChatOptions)
                                },
                                onManageChatClick = {
                                    onAction(ChatDetailAction.OnChatMembersClick)
                                },
                                onLeaveChatClick = {
                                    onAction(ChatDetailAction.OnLeaveChatClick)
                                },
                                onBackClick = {
                                    onAction(ChatDetailAction.OnBackClick)
                                }
                            )

                        }
                        MessageList(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            messageWithOpenMenu = state.messageWithOpenMenu,
                            messages = state.messages,
                            listState = messageListState,
                            onMessageLongClick = { message ->
                                onAction(ChatDetailAction.OnMessageLongClick(message))
                            },
                            onMessageRetryClick = { message ->
                                onAction(ChatDetailAction.OnRetryClick(message))
                            },
                            onDismissMessageMenu = {
                                onAction(ChatDetailAction.OnDismissMessageMenu)
                            },
                            onDeleteMessageClick = { message ->
                                onAction(ChatDetailAction.OnDeleteMessageClick(message))
                            }
                        )
                        AnimatedVisibility(
                            visible = !configuration.isWideScreen
                        ) {
                            MessageBox(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        vertical = 8.dp,
                                        horizontal = 16.dp
                                    ),
                                messageTextFieldState = state.messageTextFieldState,
                                connectionState = state.connectionState,
                                isButtonEnabled = state.canSendMessage,
                                onSendClick = {
                                    onAction(ChatDetailAction.OnSendMessageClick)
                                }
                            )
                        }
                    }
                }
                if(configuration.isWideScreen) {
                    Spacer(Modifier.height(8.dp))
                }
                AnimatedVisibility(
                    visible = configuration.isWideScreen && state.chatUi != null
                ) {
                    DynamicRoundedCornerColumn(
                        isCornersRounded = configuration.isWideScreen
                    ) {
                        MessageBox(
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            messageTextFieldState = state.messageTextFieldState,
                            connectionState = state.connectionState,
                            isButtonEnabled = state.canSendMessage,
                            onSendClick = {
                                onAction(ChatDetailAction.OnSendMessageClick)
                            }
                        )
                    }

                }
            }
        }
    }
}


@Composable
private fun DynamicRoundedCornerColumn(
    modifier: Modifier = Modifier,
    isCornersRounded: Boolean,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .shadow(
                elevation = if (isCornersRounded) 8.dp else 0.dp,
                shape = if (isCornersRounded) RoundedCornerShape(24.dp) else RectangleShape,
                spotColor = Color.Black.copy(alpha = 0.2f)
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = if (isCornersRounded) RoundedCornerShape(24.dp) else RectangleShape,
            )
    ) {
        content()
    }
}

@Preview
@Composable
private fun ChatDetailEmptyScreenPreview() {
    ChirpTheme {
        ChatDetailScreen(
            state = ChatDetailState(),
            isDetailPresent = false,
            onAction = {},
            snackBarState = remember { SnackbarHostState() }
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
@Preview
@Composable
private fun ChatDetailMessagesScreenPreview() {
    ChirpTheme(darkTheme = true) {
        ChatDetailScreen(
            state = ChatDetailState(
                messageTextFieldState = TextFieldState(initialText = "This is my message"),
                canSendMessage = true,
                chatUi = ChatUi(
                    id = "1",
                    otherParticipantUi = listOf(
                        ChatParticipantUi("2", "Naruto Uzumaki", "NU"),
                        ChatParticipantUi("3", "Sasuke Uchiha", "SU"),
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
                ),
                messages = (1..10).map {
                    if(it % 2 == 0) {
                        MessageUi.LocalUserMessage(
                            id = Uuid.random().toString(),
                            content = "Hello world!",
                            deliveryStatus = ChatMessageDeliveryStatus.SENT,
                            formattedSentTime = UiText.DynamicString("Friday, Aug 20")
                        )
                    } else {
                        MessageUi.OtherUserMessage(
                            id = Uuid.random().toString(),
                            content = "Hello world!",
                            sender = ChatParticipantUi(
                                id = Uuid.random().toString(),
                                username = "John",
                                initials = "JO"
                            ),
                            formattedSentTime = UiText.DynamicString("Friday, Aug 20"),
                        )
                    }
                }
            ),
            isDetailPresent = true,
            onAction = {},
            snackBarState = remember { SnackbarHostState() }
        )
    }
}