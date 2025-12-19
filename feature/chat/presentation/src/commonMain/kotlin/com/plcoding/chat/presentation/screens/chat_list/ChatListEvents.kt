package com.plcoding.chat.presentation.screens.chat_list

import com.plcoding.core.presentation.util.UiText

sealed interface ChatListEvents {
    data object OnLogoutSuccess: ChatListEvents
    data class OnLogoutFailure(val error: UiText): ChatListEvents
}