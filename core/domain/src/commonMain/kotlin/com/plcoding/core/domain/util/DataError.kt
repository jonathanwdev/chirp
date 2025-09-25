package com.plcoding.core.domain.util

sealed interface DataError: Error {
    enum class Remote: DataError {
        BAD_REQUEST,
        UNAUTHORIZED,
        CONFLICT,
        FORBIDDEN,
        NOT_FOUND,
        REQUEST_TIMEOUT,
        SERVER_ERROR,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVICE_UNAVAILABLE,
        SERIALIZATION,
        UNKNOWN
    }
    enum class Local: DataError {
        DISK_FULL,
        FILE_NOT_FOUND,
        UNKNOWN
    }
}