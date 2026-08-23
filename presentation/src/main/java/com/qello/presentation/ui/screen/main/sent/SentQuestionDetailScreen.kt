package com.qello.presentation.ui.screen.main.sent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.qello.presentation.R
import com.qello.presentation.component.button.QelloBackButton
import com.qello.presentation.component.button.QelloMoreButton
import com.qello.presentation.component.button.QelloShareButton
import com.qello.presentation.component.item.QelloCommentItem
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.designsystem.QelloColorPalette
import com.qello.presentation.ui.theme.QelloTheme

private data class SentCommentUiModel(
    val username: String,
    val meta: String,
    val text: String,
    val hasPhoto: Boolean,
    val likeCount: Int,
    val showTranslate: Boolean,
)

private val mockComments = listOf(
    SentCommentUiModel(
        username = "댕댕러버",
        meta = "2시간 전 · 미국 뉴욕 · 34356km",
        text = "저는 이런 사료 먹여요! 잘 먹더라고요",
        hasPhoto = true,
        likeCount = 27,
        showTranslate = false,
    ),
    SentCommentUiModel(
        username = "Traveler",
        meta = "2시간 전 · 미국 뉴욕 · 34356km",
        text = "수의사 상담도 같이 받아보세요~",
        hasPhoto = false,
        likeCount = 27,
        showTranslate = true,
    ),
)

@Composable
fun SentQuestionDetailScreen(
    questionId: Int,
    onBack: () -> Unit,
) {
    var commentInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QelloTheme.gradient.backgroundDefault)
            .windowInsetsPadding(WindowInsets.ime.exclude(WindowInsets.navigationBars)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = QelloTheme.spacing.spacing20)
                .padding(top = QelloTheme.spacing.spacing24, bottom = QelloTheme.spacing.spacing28),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            QelloBackButton(onClick = onBack)

            Spacer(Modifier.weight(1f))

            Row(horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing16)) {
                QelloShareButton(onClick = {})
                QelloMoreButton(onClick = {})
            }
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                bottom = QelloTheme.spacing.spacing20,
            ),
        ) {
            item {
                Column(modifier = Modifier.padding(horizontal = QelloTheme.spacing.spacing20 + QelloTheme.spacing.spacing6)) {
                    QelloText(
                        text = "강아지를 새로 얻으려는데, 같이 지낼 사료 추천해요?",
                        style = QelloTheme.typography.body1,
                        color = QelloTheme.colors.label.strong,
                    )

                    QelloText(
                        text = "이번에 강아지를 새로 데려왔는데 사료를 뭘로 먹여야 할지 고민이에요.",
                        style = QelloTheme.typography.caption1,
                        color = QelloTheme.colors.label.normal1,
                        modifier = Modifier.padding(top = QelloTheme.spacing.spacing4),
                    )
                }

                // TODO: 실제 이미지(Coil AsyncImage) 연동 시 aspectRatio 강제하지 말고 원본 비율 그대로 표시
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = QelloTheme.spacing.spacing20)
                        .padding(top = QelloTheme.spacing.spacing16)
                        .height(240.dp)
                        .clip(RoundedCornerShape(QelloTheme.radius.radius20))
                        .background(QelloTheme.colors.imagefield.default),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = QelloTheme.spacing.spacing20 + QelloTheme.spacing.spacing4)
                        .padding(top = QelloTheme.spacing.spacing12, bottom = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing16)) {
                        SentDetailStat(iconRes = R.drawable.icon_comment, value = "${mockComments.size}")
                        SentDetailStat(iconRes = R.drawable.icon_heart, value = "7")
                    }

                    SentDetailStat(iconRes = R.drawable.icon_location, value = "00km")
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(QelloTheme.spacing.spacing4)
                        .background(QelloTheme.colors.background.elevatedStrong),
                )
            }

            itemsIndexed(mockComments) { index, comment ->
                QelloCommentItem(
                    username = comment.username,
                    meta = comment.meta,
                    text = comment.text,
                    hasPhoto = comment.hasPhoto,
                    likeCount = comment.likeCount,
                    showTranslate = comment.showTranslate,
                    onMoreClick = {},
                    onTranslateClick = {},
                    modifier = Modifier
                        .padding(horizontal = QelloTheme.spacing.spacing20)
                        .padding(top = if (index == 0) QelloTheme.spacing.spacing12 else QelloTheme.spacing.spacing20),
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = QelloTheme.spacing.spacing20, vertical = QelloTheme.spacing.spacing24),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing8),
        ) {
            Box(
                modifier = Modifier
                    .size(QelloTheme.iconSize.size40)
                    .clip(CircleShape)
                    .background(QelloColorPalette.Navy20)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) {},
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.icon_plus),
                    contentDescription = null,
                    tint = QelloTheme.colors.label.assistive,
                    modifier = Modifier.size(20.dp),
                )
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .clip(RoundedCornerShape(QelloTheme.radius.radius20))
                    .background(QelloColorPalette.Navy20)
                    .padding(start = 18.dp, end = QelloTheme.spacing.spacing8),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing8),
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (commentInput.isEmpty()) {
                        QelloText(
                            text = "답변을 보내보세요!",
                            style = QelloTheme.typography.caption1,
                            color = QelloTheme.textFieldColors.label.default,
                        )
                    }

                    BasicTextField(
                        value = commentInput,
                        onValueChange = { commentInput = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = QelloTheme.typography.body2.copy(color = QelloTheme.textFieldColors.label.active),
                        cursorBrush = SolidColor(QelloTheme.textFieldColors.label.active),
                        singleLine = true,
                    )
                }

                if (commentInput.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(QelloColorPalette.Navy10)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) { commentInput = "" },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.icon_arrow),
                            contentDescription = null,
                            tint = QelloColorPalette.Neutral100,
                            modifier = Modifier.size(width = 10.dp, height = 15.dp),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SentDetailStat(iconRes: Int, value: String) {
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
