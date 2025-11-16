package com.plcoding.auth.presentation.screens.forgot_password

sealed interface ForgotPasswordAction {
    data object OnSubmitClick : ForgotPasswordAction

}