package com.qello.presentation.screen.main.share

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import com.qello.presentation.common.shareImage
import com.qello.presentation.designsystem.component.button.QelloBackButton
import com.qello.presentation.designsystem.component.button.QelloLargeButton
import com.qello.presentation.designsystem.component.image.QelloFeedImage
import com.qello.presentation.designsystem.component.loading.QelloLoadingOverlay
import com.qello.presentation.designsystem.component.text.QelloText
import com.qello.presentation.designsystem.theme.QelloColorPalette
import com.qello.presentation.designsystem.theme.QelloTheme
import com.qello.presentation.model.AnswerUiModel
import kotlinx.coroutines.launch

@Composable
fun QuestionShareScreen(
    uiState: QuestionShareUiState,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(QelloTheme.gradient.backgroundDefault),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = QelloTheme.spacing.spacing20)
                    .padding(top = QelloTheme.spacing.spacing24, bottom = QelloTheme.spacing.spacing28),
            ) {
                QelloBackButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart))

                QelloText(
                    text = "질문 공유하기",
                    style = QelloTheme.typography.body1,
                    color = QelloTheme.colors.label.strong,
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            // 이 안쪽(검은 배경)이 실제로 이미지로 캡처되어 공유될 영역이다. 내용 높이만큼만 차지하고 남는 공간에서 세로 가운데 정렬한다.
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .drawWithContent {
                            graphicsLayer.record { this@drawWithContent.drawContent() }
                            drawLayer(graphicsLayer)
                        }
                        .background(QelloColorPalette.Neutral0)
                        .padding(QelloTheme.spacing.spacing20),
                ) {
                    QelloText(
                        text = "Q. ${uiState.questionText}",
                        style = QelloTheme.typography.heading2,
                        color = QelloTheme.colors.label.strong,
                    )

                    if (!uiState.bodyText.isNullOrBlank()) {
                        QelloText(
                            text = uiState.bodyText,
                            style = QelloTheme.typography.caption1,
                            color = QelloTheme.colors.label.normal1,
                            modifier = Modifier.padding(top = QelloTheme.spacing.spacing4),
                        )
                    }

                    uiState.media.firstOrNull()?.let { photo ->
                        QelloFeedImage(
                            photo = photo,
                            modifier = Modifier
                                .padding(top = QelloTheme.spacing.spacing16)
                                .clip(RoundedCornerShape(QelloTheme.radius.radius16)),
                        )
                    }

                    uiState.answers.forEach { answer ->
                        ShareAnswerItem(
                            answer = answer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = QelloTheme.spacing.spacing24),
                        )
                    }
                }
            }

            QelloLargeButton(
                text = "공유하기",
                onClick = {
                    coroutineScope.launch {
                        val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
                        shareImage(context, bitmap)
                    }
                },
                modifier = Modifier.padding(QelloTheme.spacing.spacing20),
            )
        }

        if (uiState.isLoading) {
            QelloLoadingOverlay()
        }
    }
}

@Composable
private fun ShareAnswerItem(answer: AnswerUiModel, modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(QelloTheme.iconSize.size32)
                .clip(CircleShape)
                .background(QelloTheme.colors.primary.normal),
        )

        Column(modifier = Modifier.weight(1f).padding(start = QelloTheme.spacing.spacing12)) {
            QelloText(
                text = answer.authorNickname,
                style = QelloTheme.typography.caption2,
                color = QelloTheme.colors.label.strong,
            )

            QelloText(
                text = "${answer.relativeTimeLabel} · ${answer.authorCoarseRegionCode.orEmpty()} · " +
                    answer.distanceLabel,
                style = QelloTheme.typography.caption3,
                color = QelloTheme.colors.label.neutral,
                modifier = Modifier.padding(top = QelloTheme.spacing.spacing2),
            )

            QelloText(
                text = answer.bodyText,
                style = QelloTheme.typography.caption1,
                color = QelloTheme.colors.label.normal1,
                modifier = Modifier.padding(top = QelloTheme.spacing.spacing8),
            )

            answer.media.firstOrNull()?.let { photo ->
                QelloFeedImage(
                    photo = photo,
                    modifier = Modifier
                        .padding(top = QelloTheme.spacing.spacing8)
                        .clip(RoundedCornerShape(QelloTheme.radius.radius16)),
                )
            }
        }
    }
}
