package com.plcoding.auth.presentation.screens.register

sealed interface RegisterEvent {
    data class Success(val email: String): RegisterEvent
}