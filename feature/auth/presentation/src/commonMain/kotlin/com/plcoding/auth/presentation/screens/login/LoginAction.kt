package com.plcoding.auth.presentation.screens.login

sealed interface LoginAction {
    data object OnTogglePasswordVisibility : LoginAction
    data object OnLoginClick : LoginAction
    data object OnSignUpClick : LoginAction
    data object OnForgotPasswordClick : LoginAction

}