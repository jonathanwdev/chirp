package com.plcoding.core.presentation.permissions

import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.Permission
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.notifications.REMOTE_NOTIFICATION

actual class PermissionController(
    private val mokoPermissionController: PermissionsController
) {
    actual suspend fun requestPermission(permissions: Permissions): PermissionState {
        return try {
            mokoPermissionController.providePermission(permissions.toMokoPermissions())
            PermissionState.GRANTED
        } catch (_: DeniedAlwaysException) {
            PermissionState.PERMANENTLY_DENIED
        } catch (_: DeniedException) {
            PermissionState.DENIED
        } catch (_: RequestCanceledException) {
            PermissionState.DENIED
        }
    }
}

fun Permissions.toMokoPermissions(): Permission {
    return when (this) {
        Permissions.NOTIFICATIONS -> Permission.REMOTE_NOTIFICATION
    }
}