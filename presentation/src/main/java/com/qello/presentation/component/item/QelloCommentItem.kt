package com.qello.presentation.component.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import com.qello.presentation.ui.theme.QelloTheme

@Composable
fun QelloCommentItem(
    username: String,
    meta: String,
    text: String,
    hasPhoto: Boolean,
    likeCount: Int,
    showTranslate: Boolean,
    onMoreClick: () -> Unit,
    onTranslateClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(QelloTheme.iconSize.size32)
                .clip(CircleShape)
                .background(QelloTheme.colors.primary.normal),
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = QelloTheme.spacing.spacing12),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    QelloText(
                        text = username,
                        style = QelloTheme.typography.caption2,
                        color = QelloTheme.colors.label.strong,
                    )

                    QelloText(
                        text = meta,
                        style = QelloTheme.typography.caption3,
                        color = QelloTheme.colors.label.neutral,
                        modifier = Modifier.padding(top = QelloTheme.spacing.spacing2),
                    )
                }

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
                text = text,
                style = QelloTheme.typography.caption1,
                color = QelloTheme.colors.label.normal1,
                modifier = Modifier.padding(top = QelloTheme.spacing.spacing8),
            )

            if (hasPhoto) {
                // TODO: 실제 이미지(Coil AsyncImage) 연동 시 aspectRatio 강제하지 말고 원본 비율 그대로 표시
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = QelloTheme.spacing.spacing8)
                        .height(160.dp)
                        .clip(RoundedCornerShape(QelloTheme.radius.radius16))
                        .background(QelloTheme.colors.imagefield.default),
                )
            }

            Row(
                modifier = Modifier.padding(top = QelloTheme.spacing.spacing8),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing16),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing4),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.icon_heart),
                        contentDescription = null,
                        tint = QelloTheme.colors.label.assistive,
                        modifier = Modifier.size(QelloTheme.iconSize.size16),
                    )

                    QelloText(
                        text = "$likeCount",
                        style = QelloTheme.typography.caption3,
                        color = QelloTheme.colors.label.alternative,
                    )
                }

                if (showTranslate) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing4),
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onTranslateClick,
                        ),
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_translate),
                            contentDescription = null,
                            tint = QelloTheme.colors.label.assistive,
                            modifier = Modifier.size(QelloTheme.iconSize.size16),
                        )

                        QelloText(
                            text = "번역하기",
                            style = QelloTheme.typography.caption3,
                            color = QelloTheme.colors.label.alternative,
                        )
                    }
                }
            }
        }
    }
}
