package com.plcoding.auth.presentation.screens.register_success

sealed interface RegisterSuccessAction {
    data object OnLoginClick : RegisterSuccessAction
    data object OnResendVerification : RegisterSuccessAction
}