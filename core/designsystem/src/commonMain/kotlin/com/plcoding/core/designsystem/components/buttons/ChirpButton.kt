package com.plcoding.core.designsystem.components.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.plcoding.core.designsystem.components.buttons.ChirpButtonStyle
import com.plcoding.core.designsystem.theme.ChirpTheme
import com.plcoding.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview


enum class ChirpButtonStyle {
    PRIMARY,
    DESTRUCTIVE_PRIMARY,
    SECONDARY,
    DESTRUCTIVE_SECONDARY,
    TEXT
}

@Composable
fun ChirpButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit,
    style: ChirpButtonStyle = ChirpButtonStyle.PRIMARY,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val colors = when (style) {
        ChirpButtonStyle.PRIMARY -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.extended.disabledFill,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )

        ChirpButtonStyle.DESTRUCTIVE_PRIMARY -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
            disabledContainerColor = MaterialTheme.colorScheme.extended.disabledFill,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )

        ChirpButtonStyle.SECONDARY -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.extended.textSecondary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )

        ChirpButtonStyle.DESTRUCTIVE_SECONDARY -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.error,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )

        ChirpButtonStyle.TEXT -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.tertiary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled
        )
    }

    val defaultBorderStroke = BorderStroke(
        width = 1.dp,
        color = MaterialTheme.colorScheme.extended.disabledOutline
    )

    val border = when {
        style == ChirpButtonStyle.PRIMARY && !enabled -> defaultBorderStroke
        style == ChirpButtonStyle.SECONDARY -> defaultBorderStroke
        style == ChirpButtonStyle.DESTRUCTIVE_PRIMARY && !enabled -> defaultBorderStroke
        style == ChirpButtonStyle.DESTRUCTIVE_SECONDARY -> {
            BorderStroke(
                width = 1.dp,
                color = if (enabled) {
                    MaterialTheme.colorScheme.extended.destructiveSecondaryOutline
                } else {
                    MaterialTheme.colorScheme.extended.disabledOutline
                }
            )

        }
        else -> null
    }

    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        colors = colors,
        border = border,
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier.padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(15.dp).alpha(alpha = if (isLoading) 1f else 0f),
                strokeWidth = 1.5.dp,
                color = Color.Black
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    8.dp,
                    Alignment.CenterHorizontally
                ),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.alpha(
                    if (isLoading) 0f else 1f
                )
            ) {
                leadingIcon?.invoke()
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}


@Preview
@Composable
private fun ChirpButtonPrimaryPreview() {
    ChirpTheme {
        ChirpButton(
            text = "Hello world",
            onClick = {}
        )
    }
}


@Preview
@Composable
private fun ChirpButtonSecondaryPreview() {
    ChirpTheme {
        ChirpButton(
            text = "Hello world",
            style = ChirpButtonStyle.SECONDARY,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun ChirpButtonDestructivePrimaryPreview() {
    ChirpTheme {
        ChirpButton(
            text = "Hello world",
            style = ChirpButtonStyle.DESTRUCTIVE_PRIMARY,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun ChirpButtonDestructiveSecondaryPreview() {
    ChirpTheme {
        ChirpButton(
            text = "Hello world",
            style = ChirpButtonStyle.DESTRUCTIVE_SECONDARY,
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun ChirpButtonTextPreview() {
    ChirpTheme {
        ChirpButton(
            text = "Hello world",
            style = ChirpButtonStyle.TEXT,
            onClick = {}
        )
    }
}