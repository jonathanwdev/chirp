package com.plcoding.core.data.utils

actual object PlatformUtils {
    actual fun getOsName(): String {
        return System.getProperty("os.name")
    }
}