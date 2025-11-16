package com.plcoding.auth.presentation.screens.reset_password

sealed interface ResetPasswordAction {
    data object OnSubmitClick: ResetPasswordAction
    data object OnTogglePasswordVisibility: ResetPasswordAction

}