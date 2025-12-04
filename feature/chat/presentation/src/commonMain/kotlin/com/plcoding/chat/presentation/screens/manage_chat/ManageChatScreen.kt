package com.plcoding.chat.presentation.screens.manage_chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.chat_members
import chirp.feature.chat.presentation.generated.resources.create_chat
import chirp.feature.chat.presentation.generated.resources.save
import com.plcoding.chat.presentation.components.manage_chat.ManageChatAction
import com.plcoding.chat.presentation.components.manage_chat.ManageChatScreen
import com.plcoding.core.designsystem.components.dialogs.ChirpAdaptiveDialogSheetLayout
import com.plcoding.core.presentation.util.ObserveAsEvent
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ManageChatRoot(
    chatId: String?,
    onMembersAdded: () -> Unit,
    onDismiss: () -> Unit,
    viewModel: ManageChatViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvent(viewModel.events) { event ->
        when(event) {
            is ManageChatEvent.OnMembersAdded -> onMembersAdded()
        }

    }

    LaunchedEffect(chatId) {
        viewModel.onAction(ManageChatAction.ChatParticipants.OnSelectChat(chatId))
    }

    ChirpAdaptiveDialogSheetLayout(
        onDismiss = onDismiss
    ) {
        ManageChatScreen(
            headerText = stringResource(Res.string.chat_members),
            primaryButtonText = stringResource(Res.string.save),
            state = state,
            onAction = { action ->
                when(action) {
                    ManageChatAction.OnDismissDialog -> onDismiss()
                    else -> Unit
                }
                viewModel.onAction(action)
            }
        )
    }
}