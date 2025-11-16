package com.plcoding.auth.presentation.screens.login

sealed interface LoginEvent {
    data object Success: LoginEvent
}