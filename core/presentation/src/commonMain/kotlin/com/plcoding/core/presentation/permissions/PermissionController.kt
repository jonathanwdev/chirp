package com.plcoding.core.presentation.permissions

expect class PermissionController {
    suspend fun requestPermission(permissions: Permissions) : PermissionState
}