package com.qello.presentation.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.designsystem.QelloStateThemeColors
import com.qello.presentation.ui.theme.QelloTheme

@Composable
private fun QelloButtonBase(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier,
    colors: QelloStateThemeColors,
    contentPadding: PaddingValues,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isHovered by interactionSource.collectIsHoveredAsState()

    val containerColor = when {
        isPressed -> colors.container.press
        isHovered -> colors.container.hover
        else -> colors.container.default
    }
    val labelColor = when {
        isPressed -> colors.label.press
        isHovered -> colors.label.hover
        else -> colors.label.default
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(QelloTheme.radius.radius16))
            .background(containerColor)
            .hoverable(interactionSource = interactionSource)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(contentPadding),
        contentAlignment = Alignment.Center,
    ) {
        QelloText(text = text, style = QelloTheme.typography.button, color = labelColor)
    }
}

@Composable
fun QelloLargeButton(
    text: String,
    modifier: Modifier = Modifier,
    colors: QelloStateThemeColors = QelloTheme.buttonColors.lightButtonColors,
    onClick: () -> Unit,
) {
    QelloButtonBase(
        text = text,
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = colors,
        contentPadding = PaddingValues(vertical = QelloTheme.spacing.spacing16),
    )
}

@Composable
fun QelloSmallButton(
    text: String,
    modifier: Modifier = Modifier,
    colors: QelloStateThemeColors = QelloTheme.buttonColors.lightButtonColors,
    onClick: () -> Unit,
) {
    QelloButtonBase(
        text = text,
        onClick = onClick,
        modifier = modifier.size(
            width = 182.dp,
            height = 56.dp
        ),
        colors = colors,
        contentPadding = PaddingValues(
            vertical = QelloTheme.spacing.spacing16,
        ),
    )
}

@Composable
fun QelloIconButton(
    painter: Painter,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    size: Dp = QelloTheme.iconSize.size24,
    tint: Color = QelloTheme.colors.label.strong,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Icon(
        painter = painter,
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier
            .size(size)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick,
            ),
    )
}
