package com.plcoding.chat.presentation.screens.chat_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import chirp.feature.chat.presentation.generated.resources.Res
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import org.jetbrains.compose.ui.tooling.preview.Preview
import chirp.feature.chat.presentation.generated.resources.cloud_off_icon
import chirp.feature.chat.presentation.generated.resources.send
import chirp.feature.chat.presentation.generated.resources.send_a_message
import com.plcoding.chat.domain.models.ConnectionState
import com.plcoding.chat.presentation.util.toUiText
import com.plcoding.core.designsystem.components.buttons.ChirpButton
import com.plcoding.core.designsystem.components.textFields.ChirpMultiLineTextField
import com.plcoding.core.designsystem.theme.ChirpTheme
import com.plcoding.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun MessageBox(
    modifier: Modifier = Modifier,
    messageTextFieldState: TextFieldState,
    isButtonEnabled: Boolean,
    connectionState: ConnectionState,
    onSendClick: () -> Unit,
) {
    val isConnected = connectionState == ConnectionState.CONNECTED
    ChirpMultiLineTextField(
        modifier = modifier,
        state = messageTextFieldState,
        placeholder = stringResource(Res.string.send_a_message),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Send
        ),
        onKeyboardAction = onSendClick,
        bottomContent = {
            Spacer(Modifier.weight(1f))
            if(!isConnected) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.cloud_off_icon),
                        contentDescription = connectionState.toUiText().asString(),
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.extended.textDisabled
                    )
                    Text(
                        text = connectionState.toUiText().asString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.extended.textDisabled
                    )
                }
            }
            ChirpButton(
                text = stringResource(Res.string.send),
                onClick = onSendClick,
                enabled = isConnected && isButtonEnabled
            )

        }
    )
}

@Preview
@Composable
fun MessageBoxOnlinePreview() {
    ChirpTheme {
        MessageBox(
            messageTextFieldState = rememberTextFieldState(
                initialText = "Hello text field"
            ),
            isButtonEnabled = true,
            connectionState = ConnectionState.CONNECTED,
            onSendClick = {}
        )
    }
}

@Preview
@Composable
fun MessageBoxOfflinePreview() {
    ChirpTheme {
        MessageBox(
            messageTextFieldState = rememberTextFieldState(),
            isButtonEnabled = true,
            connectionState = ConnectionState.DISCONNECTED,
            onSendClick = {}
        )
    }
}

@Preview
@Composable
fun MessageBoxDarkPreview() {
    ChirpTheme(darkTheme = true) {
        MessageBox(
            messageTextFieldState = rememberTextFieldState(),
            isButtonEnabled = true,
            connectionState = ConnectionState.DISCONNECTED,
            onSendClick = {}
        )
    }
}



