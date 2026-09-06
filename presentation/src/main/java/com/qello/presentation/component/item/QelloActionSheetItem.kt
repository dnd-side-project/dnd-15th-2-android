package com.qello.presentation.component.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.designsystem.QelloColorPalette
import com.qello.presentation.ui.theme.QelloTheme

@Composable
fun QelloActionSheetItem(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(QelloTheme.radius.radius20))
            .background(QelloColorPalette.Navy20)
            .border(
                QelloTheme.borderWidth.border1,
                QelloTheme.colors.line.normal,
                RoundedCornerShape(QelloTheme.radius.radius20),
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = QelloTheme.spacing.spacing8, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing8),
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(QelloTheme.colors.primary.normal),
        )

        QelloText(
            text = text,
            style = QelloTheme.typography.body1,
            color = QelloTheme.colors.label.strong,
        )
    }
}
