package com.plcoding.chat.presentation.screens.chat_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
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
import com.plcoding.chat.presentation.screens.chat_detail.components.DateChip
import com.plcoding.chat.presentation.screens.chat_detail.components.MessageBannerListener
import com.plcoding.chat.presentation.screens.chat_detail.components.MessageBox
import com.plcoding.chat.presentation.screens.chat_detail.components.MessageList
import com.plcoding.chat.presentation.screens.chat_detail.components.PaginationScrollListener
import com.plcoding.core.designsystem.components.avatar.ChatParticipantUi
import com.plcoding.core.designsystem.theme.ChirpTheme
import com.plcoding.core.designsystem.theme.extended
import com.plcoding.core.presentation.util.ObserveAsEvent
import com.plcoding.core.presentation.util.UiText
import com.plcoding.core.presentation.util.clearFocusOnTap
import com.plcoding.core.presentation.util.currentDeviceConfiguration
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
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
    val messageListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    ObserveAsEvent(viewModel.events) { event ->
        when (event) {
            ChatDetailEvent.OnChatLeft -> {
                onBack()
            }
            ChatDetailEvent.OnNewMessage -> {
                scope.launch {
                    messageListState.animateScrollToItem(0)
                }
            }
            is ChatDetailEvent.OnError -> {
                snackBarState.showSnackbar(event.error.asStringAsync())
            }
        }
    }

    LaunchedEffect(chatId) {
        viewModel.onAction(ChatDetailAction.OnSelectChat(chatId))
        if(chatId != null) {
            messageListState.scrollToItem(0)
        }
    }


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
        messageListState = messageListState,
        onAction = { action ->
            when (action) {
                is ChatDetailAction.OnChatMembersClick -> onChatMembersClick()
                is ChatDetailAction.OnBackClick -> onBack()
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
    messageListState: LazyListState,
    onAction: (ChatDetailAction) -> Unit,
) {
    val configuration = currentDeviceConfiguration()

    val realMessages = remember(state.messages) {
        state.messages.filter { it is MessageUi.LocalUserMessage || it is MessageUi.OtherUserMessage }.size
    }
    var headerHeight by remember {
        mutableStateOf(0.dp)
    }
    val density = LocalDensity.current


    LaunchedEffect(messageListState) {
        snapshotFlow {
            messageListState.firstVisibleItemIndex to messageListState.layoutInfo.totalItemsCount
        }.filter { (firstVisibleIndex, totalItemsCount) ->
            firstVisibleIndex >= 0 && totalItemsCount > 0
        }.collect { (firstVisibleItemIndex, _) ->
            onAction(ChatDetailAction.OnFirstVisibleIndexChanged(firstVisibleItemIndex))
        }
    }

    MessageBannerListener(
        lazyListState = messageListState,
        messages = state.messages,
        isBannerVisible = state.bannerState.isVisible,
        onShowBanner = {
            onAction(ChatDetailAction.OnTopVisibleIndexChanged(it))
        },
        onHideBanner = {
            onAction(ChatDetailAction.OnHideBanner)
        }
    )


    PaginationScrollListener(
        lazyListState = messageListState,
        itemCount = realMessages,
        isPaginationLoading = state.isPaginationLoading,
        isEndReached = state.endReached,
        onNearTop = {
            onAction(ChatDetailAction.OnScrollToTop)
        }
    )

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
                    if (state.chatUi == null) {
                        EmptySection(
                            title = stringResource(Res.string.no_chat_selected),
                            description = stringResource(Res.string.select_a_chat),
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {

                        ChatHeader(
                            modifier = Modifier
                                .onSizeChanged {
                                    headerHeight = with(density) {
                                        it.height.toDp()
                                    }
                                }
                        ) {
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
                            isPaginationLoading = state.isPaginationLoading,
                            paginationError = state.paginationError?.asString(),
                            onRetryPagination = {
                                onAction(ChatDetailAction.OnRetryPaginationClick)
                            },
                            onDeleteMessageClick = { message ->
                                onAction(ChatDetailAction.OnDeleteMessageClick(message))
                            }
                        )
                        AnimatedVisibility(
                            visible = !configuration.isWideScreen,
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
                if (configuration.isWideScreen) {
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
            AnimatedVisibility(
                visible = state.bannerState.isVisible,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(
                        top = headerHeight + 16.dp,
                    ),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                state.bannerState.formattedDate?.let {
                    DateChip(
                        date = state.bannerState.formattedDate.asString()
                    )
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
            messageListState = rememberLazyListState(),
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
                    if (it % 2 == 0) {
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
            messageListState = rememberLazyListState(),
            snackBarState = remember { SnackbarHostState() }
        )
    }
}