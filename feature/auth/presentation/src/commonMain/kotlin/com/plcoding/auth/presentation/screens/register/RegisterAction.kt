package com.plcoding.auth.presentation.screens.register

sealed interface RegisterAction {
    data object OnLoginClick: RegisterAction
    data object OnRegisterClick: RegisterAction
    data object OnForgotPasswordClick: RegisterAction
    data object OnInputTextFocusChange: RegisterAction
    data object OnTogglePasswordVisibilityClick: RegisterAction

}