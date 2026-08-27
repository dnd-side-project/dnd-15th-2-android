package com.qello.presentation.ui.screen.main.question

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.qello.presentation.R
import com.qello.presentation.component.button.QelloBackButton
import com.qello.presentation.component.button.QelloLargeButton
import com.qello.presentation.component.text.QelloText
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

        // 뒤로가기 버튼이 있는 화면(패딩 24dp + 버튼 48dp)과 버튼-텍스트 간격(57dp)을 더해 시작 위치를 맞춤
        val headerTopSpacing = 24.dp + QelloTheme.iconSize.size48 + 57.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(QelloTheme.gradient.backgroundStrong)
                .padding(horizontal = QelloTheme.spacing.spacing20),
        ) {
            Spacer(Modifier.height(headerTopSpacing))

            Column(modifier = Modifier.padding(horizontal = QelloTheme.spacing.spacing4)) {
                QelloText(
                    text = "질문 제안을 보내고 있어요!",
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
                    text = "제안해주신 질문을 소중하게 검토할게요.",
                    style = QelloTheme.typography.caption1,
                    color = QelloTheme.colors.primary.normal,
                )
            }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(QelloTheme.gradient.backgroundStrong),
        ) {
            Box(modifier = Modifier.padding(start = 18.dp, top = 24.dp)) {
                QelloBackButton(onClick = onBack)
            }

            Spacer(Modifier.height(57.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = QelloTheme.spacing.spacing20),
            ) {
                Column(modifier = Modifier.padding(horizontal = QelloTheme.spacing.spacing4)) {
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

                    QelloText(
                        text = "떠오르는 질문을 자유롭게 남겨주세요.",
                        style = QelloTheme.typography.caption1,
                        color = QelloTheme.colors.label.normal2,
                        modifier = Modifier.padding(top = QelloTheme.spacing.spacing8),
                    )
                }

                Spacer(Modifier.height(50.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(QelloTheme.radius.radius20))
                        .background(QelloTheme.textFieldColors.container)
                        .border(
                            QelloTheme.borderWidth.border1,
                            QelloTheme.textFieldColors.line.default,
                            RoundedCornerShape(QelloTheme.radius.radius20),
                        )
                        .padding(QelloTheme.spacing.spacing16),
                ) {
                    if (suggestion.isEmpty()) {
                        QelloText(
                            text = "텍스트를 입력해주세요.",
                            style = QelloTheme.typography.body2,
                            color = QelloTheme.textFieldColors.label.default,
                        )
                    }

                    BasicTextField(
                        value = suggestion,
                        onValueChange = { suggestion = it },
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = QelloTheme.typography.body2.copy(color = QelloTheme.textFieldColors.label.active),
                        cursorBrush = SolidColor(QelloTheme.textFieldColors.label.active),
                        singleLine = true,
                    )
                }
            }

            if (suggestion.isNotEmpty()) {
                QelloLargeButton(
                    text = stringResource(R.string.question_suggest_button),
                    onClick = { isSending = true },
                    modifier = Modifier.padding(horizontal = QelloTheme.spacing.spacing20, vertical = QelloTheme.spacing.spacing20),
                )
            }
        }
    }
}
