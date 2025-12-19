package com.plcoding.core.designsystem.components.textFields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.plcoding.core.designsystem.components.buttons.ChirpButton
import com.plcoding.core.designsystem.theme.ChirpTheme
import com.plcoding.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpMultiLineTextField(
    modifier: Modifier = Modifier,
    state: TextFieldState,
    placeholder: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: () -> Unit = {},
    maxHeightInLies: Int = 3,
    bottomContent: @Composable (RowScope.() -> Unit)? = null,
) {
    val textFieldFocusRequester = remember { FocusRequester() }


    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.extended.surfaceLower,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.extended.surfaceOutline,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = {
                    textFieldFocusRequester.requestFocus()
                }
            )
            .padding(
                vertical = 12.dp,
                horizontal = 16.dp
            ),
    ) {
        BasicTextField(
            state = state,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth().focusRequester(textFieldFocusRequester),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.extended.textPrimary),
            keyboardOptions = keyboardOptions,
            onKeyboardAction = {
                onKeyboardAction()
            },
            lineLimits = TextFieldLineLimits.MultiLine(
                minHeightInLines = 1,
                maxHeightInLines = maxHeightInLies
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.extended.textPrimary),
            decorator = { innerBox ->
                if (placeholder != null && state.text.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.extended.textPlaceholder,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                innerBox()

            }
        )
        bottomContent?.let {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                bottomContent(this)
            }
        }

    }
}

@Composable
@Preview
private fun ChirpMultiLineTextFieldLightPreview() {
    ChirpTheme {
        ChirpMultiLineTextField(
            state = TextFieldState(
                initialText = "Initial text",
            ),
            modifier = Modifier.widthIn(300.dp).heightIn(max = 200.dp),
            placeholder = null,
            bottomContent = {
                Spacer(modifier = Modifier.weight(1f))
                ChirpButton(
                    text = "Send",
                    onClick = {}
                )
            }
        )
    }
}


@Composable
@Preview
private fun ChirpMultiLineTextFieldDarkPreview() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpMultiLineTextField(
            state = TextFieldState(
                initialText = "Initial text",
            ),
            modifier = Modifier.widthIn(300.dp).heightIn(max = 200.dp),
            placeholder = null,
            bottomContent = {
                Spacer(modifier = Modifier.weight(1f))
                ChirpButton(
                    text = "Send",
                    onClick = {}
                )
            }
        )
    }
}