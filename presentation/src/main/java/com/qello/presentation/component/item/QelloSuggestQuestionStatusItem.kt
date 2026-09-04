package com.qello.presentation.component.item

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.qello.presentation.R
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.theme.QelloTheme

@Composable
fun QelloSuggestQuestionStatusItem(
    dotColor: Color,
    title: String,
    subtitle: String,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing12),
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(7.dp))
                .background(dotColor),
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

            QelloText(
                text = subtitle,
                style = QelloTheme.typography.body2,
                color = QelloTheme.colors.label.neutral,
                modifier = Modifier.padding(top = QelloTheme.spacing.spacing4),
            )
        }
    }
}
