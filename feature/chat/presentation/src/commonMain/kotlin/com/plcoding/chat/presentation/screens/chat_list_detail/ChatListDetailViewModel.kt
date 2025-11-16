package com.plcoding.chat.presentation.screens.chat_list_detail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChatListDetailViewModel: ViewModel() {
    private val _state = MutableStateFlow(ChatListDetailState())
    val state = _state.asStateFlow()

    fun onAction(action: ChatListDetailAction) {
        when(action) {
            is ChatListDetailAction.OnChatClick -> {
                _state.update { prevState ->
                    prevState.copy(selectedChatId = action.chatId)
                }
            }
            ChatListDetailAction.OnCreateChatClick -> {
                _state.update { prevState ->
                    prevState.copy(dialogState = DialogState.CreateChat)
                }
            }
            ChatListDetailAction.OnDismissDialog -> {
                _state.update { prevState ->
                    prevState.copy(dialogState = DialogState.Hidden)
                }
            }
            ChatListDetailAction.OnManageChatClick -> {
                _state.value.selectedChatId?.let { id ->
                    _state.update { prevState ->
                        prevState.copy(dialogState = DialogState.ManageChat(id))
                    }
                }
            }
            ChatListDetailAction.OnProfileSettingClick -> {
                _state.update { prevState ->
                    prevState.copy(dialogState = DialogState.Profile)
                }
            }
        }
    }

}