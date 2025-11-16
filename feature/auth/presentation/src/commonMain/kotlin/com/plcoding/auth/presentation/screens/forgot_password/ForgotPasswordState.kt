package com.plcoding.auth.presentation.screens.forgot_password

import androidx.compose.foundation.text.input.TextFieldState
import com.plcoding.core.presentation.util.UiText

data class ForgotPasswordState(
    val emailTextFieldState: TextFieldState = TextFieldState(),
    val isLoading: Boolean = false,
    val canSubmit: Boolean = false,
    val errorText: UiText? = null,
    val isEmailSendSuccessfully: Boolean = false,
)