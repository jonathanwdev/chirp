package com.plcoding.auth.presentation.screens.login

import androidx.compose.foundation.text.input.TextFieldState
import com.plcoding.core.presentation.util.UiText

data class LoginState(
    val emailTextField: TextFieldState = TextFieldState(),
    val passwordTextField: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val isLoginIn: Boolean = false,
    val canLogin: Boolean = false,
    val error: UiText? = null
)