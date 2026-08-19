package com.qello.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.qello.presentation.R
import com.qello.presentation.ui.theme.QelloTheme

@Composable
fun QelloNotificationItem(
    title: String,
    description: String,
    timeAgo: String,
    isUnread: Boolean,
    onClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = QelloTheme.spacing.spacing24, vertical = QelloTheme.spacing.spacing12),
        horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing12),
    ) {
        Box(
            modifier = Modifier
                .size(QelloTheme.iconSize.size24)
                .clip(RoundedCornerShape(QelloTheme.radius.radius8))
                .background(QelloTheme.colors.primary.normal),
        )

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                QelloText(
                    text = title,
                    style = QelloTheme.typography.body1,
                    color = QelloTheme.colors.label.strong,
                    modifier = Modifier.weight(1f),
                )

                Icon(
                    painter = painterResource(R.drawable.ic_more),
                    contentDescription = null,
                    tint = QelloTheme.colors.label.strong,
                    modifier = Modifier
                        .size(QelloTheme.iconSize.size24)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onMoreClick,
                        ),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = QelloTheme.spacing.spacing4),
                horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing8),
            ) {
                QelloText(
                    text = description,
                    style = QelloTheme.typography.body2,
                    color = QelloTheme.colors.label.neutral,
                    modifier = Modifier.weight(1f),
                )

                QelloText(
                    text = timeAgo,
                    style = QelloTheme.typography.caption3,
                    color = QelloTheme.colors.label.neutral,
                    modifier = Modifier.align(Alignment.Bottom),
                )
            }
        }
    }
}
