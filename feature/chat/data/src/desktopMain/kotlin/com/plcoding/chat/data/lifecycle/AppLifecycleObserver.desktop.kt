package com.plcoding.chat.data.lifecycle

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

actual class AppLifecycleObserver {
    // always connected on desktop
    actual val isInForeground: Flow<Boolean>
        get() = flowOf(true)
}