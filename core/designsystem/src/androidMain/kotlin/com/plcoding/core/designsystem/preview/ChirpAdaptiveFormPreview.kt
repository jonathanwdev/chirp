package com.plcoding.core.designsystem.preview

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import com.plcoding.core.designsystem.components.brand.ChirpBrandLogo
import com.plcoding.core.designsystem.components.layouts.ChirpAdaptiveFormLayout
import com.plcoding.core.designsystem.theme.ChirpTheme

@Composable
@PreviewScreenSizes
@PreviewLightDark
private fun ChirpAdaptiveFormLayoutLightPreview() {
    ChirpTheme {
        ChirpAdaptiveFormLayout(
            headerText = "Welcome to Chirp",
            errorText = "An error occurred",
            logo = {
                ChirpBrandLogo()
            },
            formContent = {
                Text(text = "Form Content")
                Text(text = "Form Content")
            }
        )
    }
}