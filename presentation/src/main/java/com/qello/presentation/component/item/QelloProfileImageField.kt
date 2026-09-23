package com.qello.presentation.component.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.qello.presentation.R
import com.qello.presentation.ui.designsystem.theme.QelloTheme

object QelloProfileImageFieldDefaults {
    val Size: Dp = 208.dp
    val PlaceholderWidth: Dp = 71.5.dp
    val PlaceholderHeight: Dp = 67.83.dp
}

@Composable
fun QelloProfileImageField(
    imageUri: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    size: Dp = QelloProfileImageFieldDefaults.Size,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val containerColor = if (isPressed) {
        QelloTheme.colors.imagefield.hover
    } else {
        QelloTheme.colors.imagefield.default
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(containerColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClickLabel = contentDescription,
                role = Role.Button,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (imageUri == null) {
            Image(
                painter = painterResource(R.drawable.ic_picture),
                contentDescription = null,
                modifier = Modifier.size(
                    width = QelloProfileImageFieldDefaults.PlaceholderWidth,
                    height = QelloProfileImageFieldDefaults.PlaceholderHeight,
                ),
            )
        } else {
            AsyncImage(
                model = imageUri,
                contentDescription = contentDescription,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
    }
}
