package com.plcoding.chat.presentation.components.manage_chat


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.cancel
import com.plcoding.chat.presentation.components.ChatParticipantSearchTextSection
import com.plcoding.chat.presentation.components.ChatParticipantSelectionSection
import com.plcoding.chat.presentation.components.ManageChatButtonsSection
import com.plcoding.chat.presentation.components.ManageChatHeader
import com.plcoding.core.designsystem.components.brand.ChirpHorizontalDivider
import com.plcoding.core.designsystem.components.buttons.ChirpButton
import com.plcoding.core.designsystem.components.buttons.ChirpButtonStyle
import com.plcoding.core.designsystem.theme.ChirpTheme
import com.plcoding.core.presentation.util.DeviceConfiguration
import com.plcoding.core.presentation.util.clearFocusOnTap
import com.plcoding.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ManageChatScreen(
    headerText: String,
    primaryButtonText: String,
    state: ManageChatState,
    onAction: (ManageChatAction) -> Unit,
) {
    var isTextFieldFocused by remember { mutableStateOf(false) }
    val imeHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    val isKeyboardVisible = imeHeight > 0
    val configuration = currentDeviceConfiguration()
    val shouldHideHeader = configuration == DeviceConfiguration.MOBILE_LANDSCAPE || (isKeyboardVisible && configuration != DeviceConfiguration.DESKTOP) || isTextFieldFocused

    Column(
        modifier = Modifier
            .clearFocusOnTap()
            .fillMaxWidth()
            .wrapContentHeight()
            .imePadding()
            .background(MaterialTheme.colorScheme.surface)
            .navigationBarsPadding()
    ) {
        AnimatedVisibility(
            visible = !shouldHideHeader
        ) {
            Column {
                ManageChatHeader(
                    title = headerText,
                    modifier = Modifier.fillMaxWidth(),
                    onCloseClick = {
                        onAction(ManageChatAction.OnDismissDialog)
                    }
                )
                ChirpHorizontalDivider()
            }
        }
        ChatParticipantSearchTextSection(
            queryState = state.queryTextState,
            isSearchEnabled = state.canAddParticipant,
            isLoading = state.isSearching,
            modifier = Modifier.fillMaxWidth(),
            error = state.searchError,
            onFocusChanged = {
                isTextFieldFocused = it
            },
            onAddClick = {
                onAction(ManageChatAction.OnAddClick)
            }
        )
        ChirpHorizontalDivider()
        ChatParticipantSelectionSection(
            existingParticipants = state.existingChatParticipants,
            selectableParticipants = state.selectedParticipants,
            modifier = Modifier.fillMaxWidth(),
            searchResult = state.currentSearchResult
        )
        ChirpHorizontalDivider()
        ManageChatButtonsSection(
            modifier = Modifier.fillMaxWidth(),
            error = state.submitError?.asString(),
            primaryButton = {
                ChirpButton(
                    text = primaryButtonText,
                    enabled = state.selectedParticipants.isNotEmpty(),
                    isLoading = state.isSubmitting,
                    onClick = {
                        onAction(ManageChatAction.OnPrimaryActionClick)
                    },
                )
            },
            secondaryButton = {
                ChirpButton(
                    text = stringResource(Res.string.cancel),
                    onClick = {
                        onAction(ManageChatAction.OnDismissDialog)
                    },
                    style = ChirpButtonStyle.SECONDARY
                )
            },
        )
    }
}

@Preview
@Composable
private fun Preview() {
    ChirpTheme {
        ManageChatScreen(
            headerText = "Chat Members",
            state = ManageChatState(),
            onAction = {},
            primaryButtonText = "Create Chat"
        )
    }
}