package com.plcoding.chat.presentation.screens.chat_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chirp.core.designsystem.generated.resources.log_out_icon
import chirp.core.designsystem.generated.resources.logo_chirp
import chirp.core.designsystem.generated.resources.users_icon
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.logout
import chirp.feature.chat.presentation.generated.resources.profile_settings
import com.plcoding.chat.presentation.components.ChatHeader
import com.plcoding.core.designsystem.components.avatar.ChatParticipantUi
import com.plcoding.core.designsystem.components.avatar.ChirpAvatarPhoto
import com.plcoding.core.designsystem.components.dropdown.ChirpDropDownMenu
import com.plcoding.core.designsystem.components.dropdown.DropDownItem
import com.plcoding.core.designsystem.theme.ChirpTheme
import com.plcoding.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import chirp.core.designsystem.generated.resources.Res as DesignSystemRes

@Composable
fun ChatListHeader(
    modifier: Modifier = Modifier,
    localParticipant: ChatParticipantUi?,
    isUserMenuOpen: Boolean,
    onUserAvatarClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onProfileSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    ChatHeader(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = vectorResource(DesignSystemRes.drawable.logo_chirp),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary
            )
            Text(
                text = "Chirp",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.extended.textPrimary
            )
            Spacer(modifier = Modifier.weight(1f))
            ProfileAvatarSection(
                localParticipant = localParticipant,
                isMenuOpen = isUserMenuOpen,
                onClick = onUserAvatarClick,
                onProfileSettingsClick = onProfileSettingsClick,
                onLogoutClick = onLogoutClick,
                onDismissMenu = onDismissMenu
            )
        }
    }
}


@Composable
fun ProfileAvatarSection(
    modifier: Modifier = Modifier,
    localParticipant: ChatParticipantUi?,
    isMenuOpen: Boolean,
    onClick: () -> Unit,
    onProfileSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onDismissMenu: () -> Unit,
) {
    Box(
        modifier = modifier
    ) {
        localParticipant?.let {
            ChirpAvatarPhoto(
                displayText = localParticipant.initials,
                imageUrl = localParticipant.imageUrl,
                onClick = onClick,
            )
        }
        ChirpDropDownMenu(
            isOpen = isMenuOpen,
            onDismiss = onDismissMenu,
            items = listOf(
                DropDownItem(
                    icon = vectorResource(DesignSystemRes.drawable.users_icon),
                    title = stringResource(Res.string.profile_settings),
                    contentColor = MaterialTheme.colorScheme.extended.textSecondary,
                    onClick = {
                        onProfileSettingsClick()
                    }
                ),
                DropDownItem(
                    icon = vectorResource(DesignSystemRes.drawable.log_out_icon),
                    title = stringResource(Res.string.logout),
                    contentColor = MaterialTheme.colorScheme.extended.destructiveHover,
                    onClick = {
                        onLogoutClick()
                    }
                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatListHeaderPreview() {
    ChirpTheme {
        ChatListHeader(
            localParticipant = ChatParticipantUi(
                id = "1",
                username = "Naruto Uzumaki",
                imageUrl = "",
                initials = "NA"
            ),
            isUserMenuOpen = false,
            onUserAvatarClick = {},
            onDismissMenu = {},
            onProfileSettingsClick = {},
            onLogoutClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ChatListHeaderMenuOpenPreview() {
    ChirpTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            ChatListHeader(
                localParticipant = ChatParticipantUi(
                    id = "1",
                    username = "Naruto Uzumaki",
                    imageUrl = "",
                    initials = "NA"
                ),
                isUserMenuOpen = true,
                onUserAvatarClick = {},
                onDismissMenu = {},
                onProfileSettingsClick = {},
                onLogoutClick = {}
            )
        }
    }

}

