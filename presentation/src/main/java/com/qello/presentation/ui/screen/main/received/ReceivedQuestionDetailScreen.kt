package com.qello.presentation.ui.screen.main.received

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qello.domain.model.Answer
import com.qello.domain.model.InboxCard
import com.qello.presentation.R
import com.qello.presentation.component.button.QelloBackButton
import com.qello.presentation.component.button.QelloMoreButton
import com.qello.presentation.component.button.QelloShareButton
import com.qello.presentation.component.item.QelloCommentItem
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.designsystem.QelloColorPalette
import com.qello.presentation.ui.designsystem.theme.QelloTheme
import java.time.Instant
import java.time.temporal.ChronoUnit

@Composable
fun ReceivedQuestionDetailScreen(
    questionId: Int,
    onBack: () -> Unit,
    showSnackbar: suspend (message: String) -> Unit,
    viewModel: ReceivedQuestionDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val resources = LocalResources.current
    val card = uiState.detail?.card

    var commentInput by remember { mutableStateOf("") }

    LaunchedEffect(questionId) {
        viewModel.load(questionId.toLong())
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is ReceivedQuestionDetailSideEffect.ShowSnackbar -> showSnackbar(resources.getString(effect.message))
                ReceivedQuestionDetailSideEffect.AnswerSubmitted -> commentInput = ""
            }
        }
    }

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
                        text = card?.questionText.orEmpty(),
                        style = QelloTheme.typography.body1,
                        color = QelloTheme.colors.label.strong,
                    )

                    if (!card?.bodyText.isNullOrBlank()) {
                        QelloText(
                            text = card?.bodyText.orEmpty(),
                            style = QelloTheme.typography.caption1,
                            color = QelloTheme.colors.label.normal1,
                            modifier = Modifier.padding(top = QelloTheme.spacing.spacing4),
                        )
                    }
                }

                // TODO: 실제 이미지(Coil AsyncImage) 연동 시 aspectRatio 강제하지 말고 원본 비율 그대로 표시
                if (card?.mediaIds?.isNotEmpty() == true) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = QelloTheme.spacing.spacing20)
                            .padding(top = QelloTheme.spacing.spacing16)
                            .height(240.dp)
                            .clip(RoundedCornerShape(QelloTheme.radius.radius20))
                            .background(QelloTheme.colors.imagefield.default),
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = QelloTheme.spacing.spacing20 + QelloTheme.spacing.spacing4)
                        .padding(top = QelloTheme.spacing.spacing12, bottom = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing16)) {
                        DetailStat(iconRes = R.drawable.ic_comment, value = "${card?.answerCount ?: 0}")
                        DetailStat(
                            iconRes = R.drawable.ic_heart,
                            value = "${card?.reactionCount ?: 0}",
                            tint = if (card?.reactedByMe == true) QelloColorPalette.Bule50 else QelloTheme.colors.label.alternative,
                            onClick = viewModel::onReactionToggle,
                        )
                    }

                    DetailStat(iconRes = R.drawable.ic_location, value = card.toDistanceLabel())
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(QelloTheme.spacing.spacing4)
                        .background(QelloTheme.colors.background.elevatedStrong),
                )
            }

            itemsIndexed(uiState.answers, key = { _, answer -> answer.answerId }) { index, answer ->
                QelloCommentItem(
                    username = answer.authorNickname,
                    meta = "${answer.publishedAt.toRelativeTimeLabel()} · ${answer.authorCoarseRegionCode.orEmpty()} · ${answer.toDistanceLabel()}",
                    text = answer.bodyText,
                    hasPhoto = answer.mediaIds.isNotEmpty(),
                    likeCount = answer.reactionCount.toInt(),
                    showTranslate = false,
                    liked = answer.reactedByMe,
                    onLikeClick = { viewModel.onAnswerReactionToggle(answer.answerId) },
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
                    painter = painterResource(R.drawable.ic_plus),
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
                            ) { viewModel.onAnswerSubmit(commentInput) },
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow),
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
private fun DetailStat(
    iconRes: Int,
    value: String,
    tint: Color = QelloTheme.colors.label.alternative,
    onClick: (() -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing4),
        modifier = if (onClick != null) {
            Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick,
            )
        } else {
            Modifier
        },
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(QelloTheme.iconSize.size16),
        )

        QelloText(
            text = value,
            style = QelloTheme.typography.caption3,
            color = QelloTheme.colors.label.assistive,
        )
    }
}

private fun InboxCard?.toDistanceLabel(): String {
    if (this == null) return ""
    distanceBand?.let { return it }
    val meters = distanceM ?: return ""
    return if (meters >= 1000) "${meters / 1000}km" else "${meters}m"
}

private fun Answer.toDistanceLabel(): String {
    distanceBand?.let { return it }
    val meters = distanceM ?: return ""
    return if (meters >= 1000) "${meters / 1000}km" else "${meters}m"
}

private fun String.toRelativeTimeLabel(): String {
    val minutes = ChronoUnit.MINUTES.between(Instant.parse(this), Instant.now()).coerceAtLeast(0)
    return when {
        minutes < 1 -> "방금 전"
        minutes < 60 -> "${minutes}분 전"
        minutes < 60 * 24 -> "${minutes / 60}시간 전"
        else -> "${minutes / (60 * 24)}일 전"
    }
}

