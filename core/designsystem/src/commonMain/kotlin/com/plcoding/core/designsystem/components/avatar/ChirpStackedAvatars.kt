package com.plcoding.core.designsystem.components.avatar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.plcoding.core.designsystem.theme.ChirpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpStackedAvatars(
    modifier: Modifier = Modifier,
    avatars: List<ChatParticipantUi>,
    size: AvatarSize = AvatarSize.SMALL,
    maxVisible: Int = 2,
    overlapPercentage: Float = 0.4f
) {
    val overlapOffset = -(size.dp * overlapPercentage)
    val visibleAvatars = avatars.take(maxVisible)
    val remainingCount = (avatars.size - maxVisible).coerceAtLeast(0)


    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(overlapOffset),
        verticalAlignment = Alignment.CenterVertically
    ) {
        visibleAvatars.forEach { avatarItem ->
            ChirpAvatarPhoto(
                imageUrl = avatarItem.imageUrl,
                size = size,
                displayText = avatarItem.initials

            )
        }
        if(remainingCount > 0) {
            ChirpAvatarPhoto(
                size = size,
                displayText = "$remainingCount+",
                textColor = MaterialTheme.colorScheme.primary

            )
        }
    }
}

@Composable
@Preview
private fun ChirpStackedAvatarsLightPreview() {
    ChirpTheme {
        ChirpStackedAvatars(
            avatars = listOf(
                ChatParticipantUi(
                    id = "1",
                    username = "Carl Luc",
                    initials = "CL",
                ),
                ChatParticipantUi(
                    id = "2",
                    username = "John Doe",
                    initials = "JD",
                ),
                ChatParticipantUi(
                    id = "3",
                    username = "Michael Doug",
                    initials = "MD",
                ),
                ChatParticipantUi(
                    id = "4",
                    username = "Zanc Zunc",
                    initials = "ZZ",
                ),
            )
        )
    }

}

@Composable
@Preview
private fun ChirpStackedAvatarsDarkPreview() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpStackedAvatars(
            avatars = listOf(
                ChatParticipantUi(
                    id = "1",
                    username = "Carl Luc",
                    initials = "CL",
                ),
                ChatParticipantUi(
                    id = "2",
                    username = "John Doe",
                    initials = "JD",
                ),
                ChatParticipantUi(
                    id = "3",
                    username = "Michael Doug",
                    initials = "MD",
                ),
                ChatParticipantUi(
                    id = "4",
                    username = "Zanc Zunc",
                    initials = "ZZ",
                ),
                ChatParticipantUi(
                    id = "5",
                    username = "Naruto Uzumaki",
                    initials = "NU",
                ),
                ChatParticipantUi(
                    id = "6",
                    username = "Saitama",
                    initials = "SA",
                ),
            )
        )
    }

}