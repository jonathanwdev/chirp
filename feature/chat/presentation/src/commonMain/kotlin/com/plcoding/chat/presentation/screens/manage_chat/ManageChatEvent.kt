package com.plcoding.chat.presentation.screens.manage_chat

sealed interface ManageChatEvent {
    data object OnMembersAdded: ManageChatEvent
}