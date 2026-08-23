package com.qello.presentation.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import com.qello.presentation.ui.designsystem.QelloColorPalette
import com.qello.presentation.ui.theme.QelloTheme

@Composable
internal fun QelloSquareIconButton(
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconRotation: Float = 0f,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val shape = RoundedCornerShape(QelloTheme.radius.radius12)

    Box(
        modifier = modifier
            .size(QelloTheme.iconSize.size48)
            .clip(shape)
            .background(QelloColorPalette.Navy10)
            .border(QelloTheme.borderWidth.border1, QelloTheme.colors.line.normal, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painter,
            contentDescription = null,
            tint = QelloColorPalette.Neutral90,
            modifier = Modifier.graphicsLayer { rotationZ = iconRotation },
        )
    }
}
