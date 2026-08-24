package com.qello.presentation.component.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.unit.dp
import com.qello.presentation.R
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.designsystem.QelloColorPalette
import com.qello.presentation.ui.theme.QelloTheme

@Composable
fun QelloQuestionCard(
    title: String,
    hasPhoto: Boolean,
    location: String,
    localTime: String,
    commentCount: Int,
    likeCount: Int,
    postedAt: String,
    distance: String,
    onClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier,
    badgeText: String? = null,
) {
    val cardShape = RoundedCornerShape(QelloTheme.radius.radius20)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(cardShape)
            .background(QelloTheme.colors.background.normalStrong)
            .border(QelloTheme.borderWidth.borderHalf, QelloTheme.colors.line.normal, cardShape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            ),
    ) {
        if (badgeText != null) {
            QuestionCardBadge(
                text = badgeText,
                modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom = 8.dp),
            )
        }

        if (hasPhoto) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, top = if (badgeText != null) 0.dp else 10.dp)
                    .aspectRatio(16f / 10f)
                    .clip(RoundedCornerShape(15.dp))
                    .background(QelloTheme.colors.imagefield.default),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(horizontal = QelloTheme.spacing.spacing4)
                .padding(top = QelloTheme.spacing.spacing12),
        ) {
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
                text = "$location · 현지시간 $localTime",
                style = QelloTheme.typography.caption1,
                color = QelloTheme.colors.label.normal1,
                modifier = Modifier.padding(top = QelloTheme.spacing.spacing4),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = QelloTheme.spacing.spacing20, bottom = QelloTheme.spacing.spacing16),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing16)) {
                    QuestionCardStat(iconRes = R.drawable.icon_comment, value = "$commentCount")
                    QuestionCardStat(iconRes = R.drawable.icon_heart, value = "$likeCount")
                }

                Row(horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing16)) {
                    QuestionCardStat(iconRes = R.drawable.icon_clock, value = postedAt)
                    QuestionCardStat(iconRes = R.drawable.icon_location, value = distance)
                }
            }
        }
    }
}

@Composable
private fun QuestionCardBadge(text: String, modifier: Modifier = Modifier) {
    val badgeShape = RoundedCornerShape(QelloTheme.radius.radiusFull)

    QelloText(
        text = text,
        style = QelloTheme.typography.caption2,
        color = QelloColorPalette.Orange50,
        modifier = modifier
            .clip(badgeShape)
            .background(QelloColorPalette.Orange20)
            .border(QelloTheme.borderWidth.borderHalf, QelloColorPalette.Orange50, badgeShape)
            .padding(horizontal = QelloTheme.spacing.spacing12, vertical = QelloTheme.spacing.spacing4),
    )
}

@Composable
private fun QuestionCardStat(iconRes: Int, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing4),
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = QelloTheme.colors.label.alternative,
            modifier = Modifier.size(QelloTheme.iconSize.size16),
        )

        QelloText(
            text = value,
            style = QelloTheme.typography.caption3,
            color = QelloTheme.colors.label.assistive,
        )
    }
}
