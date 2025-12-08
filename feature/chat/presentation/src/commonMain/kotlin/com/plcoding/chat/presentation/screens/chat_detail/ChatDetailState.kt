package com.plcoding.chat.presentation.screens.chat_detail

import androidx.compose.foundation.text.input.TextFieldState
import com.plcoding.chat.domain.models.ConnectionState
import com.plcoding.chat.presentation.models.ChatUi
import com.plcoding.chat.presentation.models.MessageUi
import com.plcoding.core.presentation.util.UiText

data class ChatDetailState(
    val chatUi: ChatUi? = null,
    val isLoading: Boolean = false,
    val messages: List<MessageUi> = emptyList(),
    val error: UiText? = null,
    val messageTextFieldState: TextFieldState = TextFieldState(),
    val canSendMessage: Boolean = false,
    val isPaginationLoading: Boolean = false,
    val paginationError: UiText? = null,
    val endReached: Boolean = false,
    val bannerState: BannerState = BannerState(),
    val isChatOptionsOpen: Boolean = false,
    val isNearBottom: Boolean = false,
    val messageWithOpenMenu: MessageUi.LocalUserMessage? = null,
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED
)


data class BannerState(
    val formattedDate: UiText? = null,
    val isVisible: Boolean = false,
)