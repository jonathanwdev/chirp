package com.plcoding.core.domain.validation

object PasswordValidator {
    private const val MIN_LENGTH = 9

    fun validate(password: String): PasswordValidationState {
        return PasswordValidationState(
            hasMinLength = password.length >= MIN_LENGTH,
            hasDigit = password.any{ it.isDigit() },
            hasUppercase = password.any { it.isUpperCase() }
        )
    }
}