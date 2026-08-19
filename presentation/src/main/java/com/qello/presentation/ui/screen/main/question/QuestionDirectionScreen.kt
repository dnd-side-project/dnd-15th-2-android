package com.qello.presentation.ui.screen.main.question

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.qello.presentation.component.QelloBackButton
import com.qello.presentation.component.QelloLargeButton
import com.qello.presentation.component.QelloText
import com.qello.presentation.ui.theme.QelloTheme
import kotlinx.coroutines.delay

@Composable
fun QuestionDirectionScreen(
    onBack: () -> Unit,
    direction: String = "동쪽", // TODO: 지도/방향 센서 API 연동되면 실제 값으로 교체
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
            Spacer(Modifier.height(QelloTheme.spacing.spacing64))

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
        }
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            // TODO: 실제 지도 API 연동되면 이 자리에 지도 + 방향 센서로 움직이는 콘(cone) 표시로 교체
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(QelloTheme.gradient.backgroundStrong),
            ) {
                QelloText(
                    text = "지도 영역 (API 연동 예정)",
                    style = QelloTheme.typography.caption1,
                    color = QelloTheme.colors.label.assistive,
                    modifier = Modifier.align(Alignment.Center),
                )
            }

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
                    text = "${direction}으로 질문 보내기",
                    onClick = { isSending = true },
                )
            }
        }
    }
}
