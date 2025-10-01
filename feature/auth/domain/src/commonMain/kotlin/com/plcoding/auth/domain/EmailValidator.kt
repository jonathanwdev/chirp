package com.plcoding.auth.domain

object EmailValidator {
    private const val EMAIL_PATTERN = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"

    fun validate(email: String): Boolean {
        val emailRegex = Regex(EMAIL_PATTERN)
        return emailRegex.matches(email)

    }
}