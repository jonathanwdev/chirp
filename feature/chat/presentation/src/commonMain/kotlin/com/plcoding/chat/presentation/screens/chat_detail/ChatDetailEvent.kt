package com.plcoding.chat.presentation.screens.chat_detail

import com.plcoding.core.presentation.util.UiText

sealed interface ChatDetailEvent {
    data object OnChatLeft: ChatDetailEvent
    data object OnNewMessage: ChatDetailEvent
    data class OnError(val error: UiText): ChatDetailEvent

}