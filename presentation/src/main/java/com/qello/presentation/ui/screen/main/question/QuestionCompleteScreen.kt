package com.qello.presentation.ui.screen.main.question

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.qello.presentation.component.QelloSmallButton
import com.qello.presentation.component.QelloText
import com.qello.presentation.ui.theme.QelloTheme

@Composable
fun QuestionCompleteScreen(
    primaryButtonText: String,
    direction: String = "동쪽", // TODO: 방향 선택 API 연동되면 실제 값으로 교체
    onSendAnother: () -> Unit,
    onNavigateHome: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QelloTheme.gradient.backgroundStrong)
            .padding(horizontal = QelloTheme.spacing.spacing20),
    ) {
        Spacer(Modifier.height(QelloTheme.spacing.spacing64))

        QelloText(
            text = "${direction}으로 질문을 보냈어요!",
            style = QelloTheme.typography.heading1,
            color = QelloTheme.colors.label.strong,
        )

        QelloText(
            text = "곧 답변이 도착할 거예요",
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
                .size(240.dp)
                .clip(RoundedCornerShape(QelloTheme.radius.radius24))
                .background(QelloTheme.colors.imagefield.default),
        )

        Spacer(Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = QelloTheme.spacing.spacing24),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            QelloSmallButton(
                text = primaryButtonText,
                colors = QelloTheme.buttonColors.darkButtonColors,
                onClick = onSendAnother,
            )
            QelloSmallButton(
                text = "홈으로 돌아가기",
                onClick = onNavigateHome,
            )
        }
    }
}
