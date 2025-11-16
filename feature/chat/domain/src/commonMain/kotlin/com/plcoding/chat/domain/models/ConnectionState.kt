package com.plcoding.chat.domain.models

enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ERROR_UNKNOWN,
    ERROR_NETWORK,
}

