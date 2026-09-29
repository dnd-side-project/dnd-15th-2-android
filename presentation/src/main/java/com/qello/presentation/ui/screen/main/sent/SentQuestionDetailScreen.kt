package com.qello.presentation.ui.screen.main.sent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qello.domain.model.Answer
import com.qello.presentation.R
import com.qello.presentation.component.button.QelloBackButton
import com.qello.presentation.component.button.QelloMoreButton
import com.qello.presentation.component.button.QelloShareButton
import com.qello.presentation.component.item.QelloCommentItem
import com.qello.presentation.component.loading.QelloLoadingOverlay
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.designsystem.theme.QelloTheme
import java.time.Instant
import java.time.temporal.ChronoUnit

@Composable
fun SentQuestionDetailScreen(
    questionId: Int,
    onBack: () -> Unit,
    showSnackbar: suspend (message: String) -> Unit,
    viewModel: SentQuestionDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val resources = LocalResources.current
    val card = uiState.detail?.card

    LaunchedEffect(questionId) {
        viewModel.load(questionId.toLong())
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is SentQuestionDetailSideEffect.ShowSnackbar -> showSnackbar(resources.getString(effect.message))
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(QelloTheme.gradient.backgroundDefault),
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
                            SentDetailStat(iconRes = R.drawable.ic_comment, value = "${card?.answerCount ?: 0}")
                            SentDetailStat(iconRes = R.drawable.ic_heart, value = "${card?.reactionCount ?: 0}")
                        }
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
                        meta = "${answer.publishedAt.toRelativeTimeLabel()} · ${answer.authorCoarseRegionCode.orEmpty()} · " +
                            answer.toDistanceLabel(),
                        text = answer.bodyText,
                        hasPhoto = answer.mediaIds.isNotEmpty(),
                        likeCount = answer.reactionCount.toInt(),
                        showTranslate = false,
                        onMoreClick = {},
                        onTranslateClick = {},
                        modifier = Modifier
                            .padding(horizontal = QelloTheme.spacing.spacing20)
                            .padding(top = if (index == 0) QelloTheme.spacing.spacing12 else QelloTheme.spacing.spacing20),
                    )
                }
            }
        }

        if (uiState.isLoading) {
            QelloLoadingOverlay()
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

private fun Answer.toDistanceLabel(): String {
    distanceBand?.let { return it }
    val meters = distanceM ?: return ""
    return if (meters >= 1000) "${meters / 1000}km" else "${meters}m"
}

private fun String.toRelativeTimeLabel(): String {
    val minutes = ChronoUnit.MINUTES.between(Instant.parse(this), Instant.now())
    return when {
        minutes < 1 -> "방금 전"
        minutes < 60 -> "${minutes}분 전"
        minutes < 60 * 24 -> "${minutes / 60}시간 전"
        else -> "${minutes / (60 * 24)}일 전"
    }
}
