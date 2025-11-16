package com.plcoding.chat.presentation.screens.chat_detail

import com.plcoding.chat.presentation.models.MessageUi

sealed interface ChatDetailAction {
    data object OnSendMessageClick : ChatDetailAction
    data object OnScrollToTop : ChatDetailAction
    data object OnBackClick : ChatDetailAction
    data object OnDismissChatOptions : ChatDetailAction
    data object OnChatOptionsClick : ChatDetailAction
    data object OnChatMembersClick : ChatDetailAction
    data object OnLeaveChatClick : ChatDetailAction
    data object OnDismissMessageMenu : ChatDetailAction
    data class OnSelectChat(val chatId: String?) : ChatDetailAction
    data class OnDeleteMessageClick(val message: MessageUi.LocalUserMessage) : ChatDetailAction
    data class OnMessageLongClick(val message: MessageUi.LocalUserMessage) : ChatDetailAction
    data class OnRetryClick(val message: MessageUi.LocalUserMessage) : ChatDetailAction
}