package com.plcoding.core.designsystem.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.plcoding.core.designsystem.theme.ChirpTheme
import com.plcoding.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpIconButton(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
    content: @Composable () -> Unit = {}
) {
    OutlinedIconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.size(45.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline
        ),
        colors = IconButtonDefaults.outlinedIconButtonColors(
            contentColor = MaterialTheme.colorScheme.extended.textSecondary,
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        content()
    }
}


@Preview
@Composable
private fun ChirpIconButtonPreview() {
    ChirpTheme {
        ChirpIconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null

            )
        }
    }
}


@Preview
@Composable
private fun ChirpIconButtonDarkThemePreview() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpIconButton(
            onClick = {}
        ) {
            Icon(
                imageVector = Icons.Filled.Settings,
                contentDescription = null

            )
        }
    }
}