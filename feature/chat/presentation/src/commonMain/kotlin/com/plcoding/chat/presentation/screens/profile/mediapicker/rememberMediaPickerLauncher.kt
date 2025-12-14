package com.plcoding.chat.presentation.screens.profile.mediapicker

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun rememberImagePickerLauncher(
    onResult: (PickedImageData) -> Unit,
): ImagePickerLauncher


class ImagePickerLauncher(
    private val onLaunch: () -> Unit,
) {
    fun launch() {
        onLaunch()
    }
}


class PickedImageData(
    val bytes: ByteArray,
    val mimeType: String?
)