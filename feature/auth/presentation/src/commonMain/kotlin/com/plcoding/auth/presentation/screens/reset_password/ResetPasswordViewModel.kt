package com.plcoding.auth.presentation.screens.reset_password

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.error_reset_password_token_invalid
import chirp.feature.auth.presentation.generated.resources.error_same_password
import com.plcoding.core.domain.auth.AuthService
import com.plcoding.core.domain.util.DataError
import com.plcoding.core.domain.util.onComplete
import com.plcoding.core.domain.util.onFailure
import com.plcoding.core.domain.util.onSuccess
import com.plcoding.core.domain.validation.PasswordValidator
import com.plcoding.core.presentation.util.UiText
import com.plcoding.core.presentation.util.toUiText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val authService: AuthService,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val token = savedStateHandle.get<String>("token") ?: throw IllegalStateException("No token provided")

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ResetPasswordState())

    private val isPasswordValidFlow = snapshotFlow { state.value.passwordTextState.text.toString() }
        .map { password -> PasswordValidator.validate(password).isValidPassword }
        .distinctUntilChanged()


    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeValidationState()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ResetPasswordState()
        )

    private fun observeValidationState() {
        isPasswordValidFlow.onEach { isValid ->
            _state.update { it.copy(canSubmit = isValid) }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: ResetPasswordAction) {
        when (action) {
            ResetPasswordAction.OnSubmitClick -> resetPassword()
            ResetPasswordAction.OnTogglePasswordVisibility -> {
                _state.update { it.copy(canSubmit = !it.isPasswordVisible) }
            }


        }
    }

    private fun resetPassword() {
        if(!state.value.canSubmit || state.value.isLoading) return
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    isLoading = true,
                    isResetSuccessful = false
                )
            }
            authService.resetPassword(
                password = _state.value.passwordTextState.text.toString(),
                token = token
            ).onSuccess {
                _state.update {
                    it.copy(
                        isResetSuccessful = true,
                        errorText = null
                    )
                }
            }.onFailure { error ->
                val errorText = when(error) {
                    DataError.Remote.UNAUTHORIZED -> UiText.Resource(Res.string.error_reset_password_token_invalid)
                    DataError.Remote.CONFLICT -> UiText.Resource(Res.string.error_same_password)
                    else -> error.toUiText()
                }
                _state.update {
                    it.copy(
                        errorText = errorText,
                    )
                }
            }.onComplete {
                _state.update {
                    it.copy(
                        isLoading = false,
                    )
                }
            }
        }
    }

}