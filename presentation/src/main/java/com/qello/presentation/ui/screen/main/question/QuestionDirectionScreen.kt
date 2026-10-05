package com.qello.presentation.ui.screen.main.question

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mapbox.geojson.Point
import com.qello.presentation.R
import com.qello.presentation.component.button.QelloBackButton
import com.qello.presentation.component.button.QelloLargeButton
import com.qello.presentation.component.map.QelloMap
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.designsystem.theme.QelloTheme

@Composable
fun QuestionDirectionScreen(
    bodyText: String,
    mediaId: Long?,
    approvedQuestionId: Long,
    onBack: () -> Unit,
    onSendComplete: () -> Unit,
    showSnackbar: suspend (message: String) -> Unit,
    viewModel: QuestionDirectionViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val resources = LocalResources.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                QuestionDirectionSideEffect.NavigateToComplete -> onSendComplete()
                is QuestionDirectionSideEffect.ShowSnackbar -> showSnackbar(resources.getString(effect.message))
            }
        }
    }

    if (uiState.isSending) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(QelloTheme.gradient.backgroundStrong)
                .padding(horizontal = QelloTheme.spacing.spacing20),
        ) {
            QuestionStatusHeader(
                titleLine1 = "질문을 보내고 있어요!",
                titleLine2 = "잠시만 기다려주세요.",
                caption = "켈로에서 많은 사람들과 질문하며 알아가요",
            )

            Spacer(Modifier.weight(1f))

            QuestionStatusImage(modifier = Modifier.align(Alignment.CenterHorizontally))

            Spacer(Modifier.weight(1f))

            // 완료 화면 하단 버튼(56dp)+여백(24dp)만큼 자리를 비워둬서 아이콘 위치를 맞춤
            Spacer(Modifier.height(56.dp + QelloTheme.spacing.spacing24))
        }
    } else {
        // 방향 설정(지도) 화면이 보이는 동안에만 지도(+위치 표시)를 켠다. 전송 중 화면으로 넘어가면 자동으로 꺼진다.
        var bearingDegrees by remember { mutableStateOf<Float?>(null) }
        val direction = bearingDegrees?.let(CompassDirection::fromBearing)

        Box(modifier = Modifier.fillMaxSize()) {
            // 실제 위치를 알기 전까지만 보여줄 기본 좌표. 위치를 알게 되면 QelloMap이 그리로 한 번 옮겨준다.
            QelloMap(
                initialCenter = Point.fromLngLat(126.9780, 37.5665),
                initialZoom = 10.0,
                modifier = Modifier.fillMaxSize(),
                showsUserLocation = true,
                showsDirectionCone = true,
                onBearingChanged = { degrees -> bearingDegrees = degrees },
            )

            Box(modifier = Modifier.padding(start = 18.dp, top = 24.dp)) {
                QelloBackButton(onClick = onBack)
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = QelloTheme.spacing.spacing20)
                    .padding(bottom = QelloTheme.spacing.spacing24),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                QelloText(
                    text = "핸드폰을 움직여 보내는 방향을 바꿔보세요!",
                    style = QelloTheme.typography.caption1.copy(textAlign = TextAlign.Center),
                    color = QelloTheme.colors.label.strong,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(QelloTheme.spacing.spacing20))
                QelloLargeButton(
                    text = direction?.let { stringResource(R.string.direction_send_button, stringResource(it.labelRes)) }
                        ?: stringResource(R.string.direction_finding_button),
                    onClick = {
                        if (direction != null) {
                            viewModel.onSendClick(
                                bodyText = bodyText,
                                mediaId = mediaId,
                                approvedQuestionId = approvedQuestionId,
                                segmentKey = direction.segmentKey,
                            )
                        }
                    },
                )
            }
        }
    }
}
