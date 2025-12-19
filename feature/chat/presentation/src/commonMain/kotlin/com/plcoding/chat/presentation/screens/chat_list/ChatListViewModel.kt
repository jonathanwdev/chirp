package com.plcoding.chat.presentation.screens.chat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.chat.domain.chat.ChatRepository
import com.plcoding.chat.domain.notification.DeviceTokenService
import com.plcoding.chat.domain.participant.ChatParticipantRepository
import com.plcoding.chat.presentation.mappers.toUi
import com.plcoding.core.domain.auth.AuthService
import com.plcoding.core.domain.auth.SessionStorage
import com.plcoding.core.domain.util.onFailure
import com.plcoding.core.domain.util.onSuccess
import com.plcoding.core.presentation.util.toUiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val chatRepository: ChatRepository,
    private val sessionStorage: SessionStorage,
    private val deviceTokenService: DeviceTokenService,
    private val authService: AuthService,
    private val chatParticipantRepository: ChatParticipantRepository,
) : ViewModel() {
    private val eventChannel = Channel<ChatListEvents>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ChatListState())
    val state = combine(
        _state,
        chatRepository.getChats(),
        sessionStorage.observeAuthInfo()
    ){ currentState, chats, sessionInfo ->
        if(sessionInfo == null) {
            return@combine ChatListState()
        }
        currentState.copy(
            chats = chats.map { it.toUi(sessionInfo.user.id) },
            localParticipant = sessionInfo.user.toUi()
        )
    }
        .onStart {
            if (!hasLoadedInitialData) {
                loadChats()
                fetchLocalUserProfile()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ChatListState()
        )

    private fun fetchLocalUserProfile() {
        viewModelScope.launch {
            chatParticipantRepository.fetchLocalParticipant()
        }
    }

    private fun showLogoutConfirmation() {
        _state.update { it.copy(isUserMenuOpen = false, showLogoutConfirmation = true) }
    }

    fun onAction(action: ChatListAction) {
        when (action) {
            is ChatListAction.OnSelectChat ->  {
                _state.update { it.copy(selectedChatId = action.chatId) }
            }
            ChatListAction.OnUserAvatarClick -> {
                _state.update { it.copy(isUserMenuOpen = true) }
            }
            ChatListAction.OnLogoutClick -> showLogoutConfirmation()
            ChatListAction.OnConfirmLogout -> logout()
            ChatListAction.OnDismissLogoutDialog -> {
                _state.update { it.copy(showLogoutConfirmation = false) }
            }
            ChatListAction.OnProfileSettingsClick,
            ChatListAction.OnDismissMenu -> {
                _state.update { it.copy(isUserMenuOpen = false) }
            }
            else -> Unit
        }
    }

    private fun logout() {
        _state.update { it.copy(showLogoutConfirmation = false) }
        viewModelScope.launch {
            delay(100)
            val authInfo = sessionStorage.observeAuthInfo().first()
            val refreshToken = authInfo?.refreshToken ?: return@launch
            deviceTokenService
                .unregisterToken(refreshToken)
                .onSuccess {
                    authService
                        .logout(refreshToken)
                        .onSuccess {
                            sessionStorage.set(null)
                            chatRepository.deleteAllChats()
                            eventChannel.send(ChatListEvents.OnLogoutSuccess)
                        }
                        .onFailure { failure ->
                            eventChannel.send(ChatListEvents.OnLogoutFailure(failure.toUiText()))
                        }
                }
                .onFailure { failure ->
                    eventChannel.send(ChatListEvents.OnLogoutFailure(failure.toUiText()))
                }

        }
    }

    private fun loadChats() {
        viewModelScope.launch(Dispatchers.IO) {
            chatRepository.fetchChats()
        }
    }

}