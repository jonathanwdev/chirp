package com.plcoding.chat.presentation.screens.profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.drop_picture_info
import chirp.feature.chat.presentation.generated.resources.upload_icon
import chirp.feature.chat.presentation.generated.resources.upload_image
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun DragAndDropOverlay() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = .8f)),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = vectorResource(Res.drawable.upload_icon),
            contentDescription = stringResource(Res.string.upload_image),
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.surface
        )
        Text(
            text = stringResource(Res.string.drop_picture_info),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.surface
        )
    }
}