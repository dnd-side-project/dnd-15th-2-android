package com.qello.presentation.ui.screen.main.question

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.qello.presentation.component.QelloBackButton
import com.qello.presentation.component.QelloLargeButton
import com.qello.presentation.component.QelloText
import com.qello.presentation.component.QelloTextArea
import com.qello.presentation.ui.theme.QelloTheme
import kotlinx.coroutines.delay

@Composable
fun QuestionSuggestComposeScreen(
    onBack: () -> Unit,
    onSendComplete: () -> Unit,
) {
    var suggestion by remember { mutableStateOf("") }
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(QelloTheme.gradient.backgroundStrong),
        ) {
            Box(modifier = Modifier.padding(start = 18.dp, top = 24.dp)) {
                QelloBackButton(onClick = onBack)
            }

            Spacer(Modifier.height(QelloTheme.spacing.spacing16))

            Column(modifier = Modifier.padding(horizontal = QelloTheme.spacing.spacing20)) {
                QelloText(
                    text = "어떤 질문을",
                    style = QelloTheme.typography.heading1,
                    color = QelloTheme.colors.label.strong,
                )
                QelloText(
                    text = "제안하고 싶나요?",
                    style = QelloTheme.typography.heading1,
                    color = QelloTheme.colors.label.strong,
                )

                Spacer(Modifier.height(QelloTheme.spacing.spacing24))

                QelloTextArea(
                    value = suggestion,
                    onValueChange = { suggestion = it },
                )

                Spacer(Modifier.height(QelloTheme.spacing.spacing20))

                QelloLargeButton(
                    text = "질문 제안하기",
                    onClick = { isSending = true },
                )
            }
        }
    }
}
