package com.plcoding.core.designsystem.components.textFields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import chirp.core.designsystem.generated.resources.Res
import chirp.core.designsystem.generated.resources.eye_icon
import chirp.core.designsystem.generated.resources.eye_off_icon
import chirp.core.designsystem.generated.resources.hide_password
import chirp.core.designsystem.generated.resources.show_password
import com.plcoding.core.designsystem.theme.ChirpTheme
import com.plcoding.core.designsystem.theme.extended
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpPasswordTextField(
    modifier: Modifier = Modifier,
    isPasswordVisible: Boolean,
    onToggleVisibilityClick: () -> Unit,
    state: TextFieldState,
    placeholder: String? = null,
    title: String? = null,
    enabled: Boolean = true,
    supportingText: String? = null,
    isError: Boolean = false,
    onFocusChanged: (Boolean) -> Unit = {},
) {
    ChirpTextFieldLayout(
        title = title,
        isError = isError,
        supportingText = supportingText,
        enabled = enabled,
        onFocusChanged = onFocusChanged,
        modifier = modifier
    ) { styledModifier, interactionSource ->
        BasicSecureTextField(
            state = state,
            modifier = styledModifier,
            enabled = enabled,
            textObfuscationMode = if (isPasswordVisible) TextObfuscationMode.Visible else TextObfuscationMode.Hidden,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.extended.textPlaceholder
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            interactionSource = interactionSource,
            decorator = { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (state.text.isEmpty() && placeholder != null) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.extended.textPlaceholder
                            )
                        }
                        innerTextField()
                    }
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(
                                    bounded = false,
                                    radius = 20.dp
                                ),
                                onClick = onToggleVisibilityClick
                            ),
                        tint = MaterialTheme.colorScheme.extended.textDisabled,
                        imageVector = if (isPasswordVisible) {
                            vectorResource(Res.drawable.eye_off_icon)
                        } else vectorResource(Res.drawable.eye_icon),
                        contentDescription = if (isPasswordVisible) {
                            stringResource(Res.string.hide_password)
                        } else stringResource(Res.string.show_password)
                    )
                }
            }

        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ChirpPasswordTextFieldEmptyPreview() {
    ChirpTheme {
        ChirpPasswordTextField(
            state = rememberTextFieldState(),
            modifier = Modifier.width(300.dp),
            placeholder = "Any placeholder",
            title = "Password",
            isError = false,
            isPasswordVisible = false,
            onToggleVisibilityClick = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ChirpPasswordTextFieldFilledPreview() {
    ChirpTheme {
        ChirpPasswordTextField(
            state = rememberTextFieldState(initialText = "123456"),
            modifier = Modifier.width(300.dp),
            placeholder = "Any placeholder",
            title = "Password",
            isError = false,
            isPasswordVisible = false,
            onToggleVisibilityClick = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ChirpPasswordTextFieldErrorPreview() {
    ChirpTheme {
        ChirpPasswordTextField(
            state = rememberTextFieldState(initialText = "123456"),
            modifier = Modifier.width(300.dp),
            placeholder = "Any placeholder",
            supportingText = "Invalid Password",
            title = "Password",
            isError = true,
            isPasswordVisible = false,
            onToggleVisibilityClick = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun ChirpPasswordTextFieldVisiblePreview() {
    ChirpTheme {
        ChirpPasswordTextField(
            state = rememberTextFieldState(initialText = "123456"),
            modifier = Modifier.width(300.dp),
            placeholder = "Any placeholder",
            supportingText = "Invalid Password",
            title = "Password",
            isError = false,
            isPasswordVisible = true,
            onToggleVisibilityClick = {}
        )
    }
}