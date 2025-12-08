package com.plcoding.chat.presentation.screens.chat_list_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.chat.domain.chat.ChatConnectionClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ChatListDetailViewModel(
    private val connectionClient: ChatConnectionClient
): ViewModel() {
    private var hasLoadedInitialData = false
    private val _state = MutableStateFlow(ChatListDetailState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                connectionClient.chatMessages.launchIn(viewModelScope)
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ChatListDetailState()
        )

    fun onAction(action: ChatListDetailAction) {
        when(action) {
            is ChatListDetailAction.OnSelectChat -> {
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