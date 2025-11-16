package com.plcoding.chat.presentation.screens.chat_list

import com.plcoding.chat.presentation.models.ChatUi

sealed interface ChatListAction {
    data object OnUserAvatarClick : ChatListAction
    data object OnDismissMenu: ChatListAction
    data object OnLogoutClick: ChatListAction
    data object OnConfirmLogout: ChatListAction
    data object OnDismissLogoutDialog: ChatListAction
    data object OnCreateChatClick: ChatListAction
    data object OnProfileSettingsClick: ChatListAction
    data class OnChatClick(val chat: ChatUi): ChatListAction
}