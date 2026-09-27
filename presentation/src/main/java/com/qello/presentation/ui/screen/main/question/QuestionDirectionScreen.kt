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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.qello.presentation.R
import com.qello.presentation.component.button.QelloBackButton
import com.qello.presentation.component.button.QelloLargeButton
import com.qello.presentation.component.map.QelloMap
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.sensor.rememberCompassBearing
import com.qello.presentation.ui.designsystem.theme.QelloTheme
import kotlinx.coroutines.delay

@Composable
fun QuestionDirectionScreen(
    onBack: () -> Unit,
    onSendComplete: () -> Unit,
) {
    var isSending by remember { mutableStateOf(false) }

    if (isSending) {
        LaunchedEffect(Unit) {
            delay(1500)
            onSendComplete()
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(QelloTheme.gradient.backgroundStrong)
                .padding(horizontal = QelloTheme.spacing.spacing20),
        ) {
            Spacer(Modifier.height(24.dp + QelloTheme.iconSize.size48))

            QelloText(
                text = "질문을 보내고 있어요!",
                style = QelloTheme.typography.heading1,
                color = QelloTheme.colors.label.strong,
            )

            QelloText(
                text = "잠시만 기다려주세요.",
                style = QelloTheme.typography.heading1,
                color = QelloTheme.colors.label.strong,
            )

            Spacer(Modifier.height(QelloTheme.spacing.spacing8))

            QelloText(
                text = "켈로에서 많은 사람들과 질문하며 알아가요",
                style = QelloTheme.typography.caption1,
                color = QelloTheme.colors.primary.normal,
            )

            Spacer(Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(300.dp)
                    .clip(RoundedCornerShape(QelloTheme.radius.radius24))
                    .background(QelloTheme.colors.imagefield.default),
            )

            Spacer(Modifier.weight(1f))

            // 완료 화면 하단 버튼(56dp)+여백(24dp)만큼 자리를 비워둬서 아이콘 위치를 맞춤
            Spacer(Modifier.height(56.dp + QelloTheme.spacing.spacing24))
        }
    } else {
        // 방향 설정(지도) 화면이 보이는 동안에만 센서를 확인한다. 전송 중 화면으로 넘어가면 자동으로 꺼진다.
        val bearing = rememberCompassBearing()
        val direction by remember { derivedStateOf { bearing.value?.let(CompassDirection::fromBearing) } }

        Box(modifier = Modifier.fillMaxSize()) {
            // TODO: 실제 위치 연동되면 고정 좌표 대신 현재 위치로 교체, 방향 센서로 움직이는 콘(cone) 표시 추가
            QelloMap(
                initialCenter = Point.fromLngLat(126.9780, 37.5665),
                initialZoom = 10.0,
                modifier = Modifier.fillMaxSize(),
            )

            Box(
                modifier = Modifier
                    .padding(start = 18.dp, top = 24.dp),
            ) {
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
                    // TODO: 질문 보내기 API 연동되면 실제 전송으로 교체
                    onClick = { if (direction != null) isSending = true },
                )
            }
        }
    }
}
