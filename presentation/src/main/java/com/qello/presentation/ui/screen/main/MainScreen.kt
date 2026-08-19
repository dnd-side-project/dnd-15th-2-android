package com.qello.presentation.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.qello.presentation.component.bottombar.HomeBottomBarTab
import com.qello.presentation.component.bottombar.QelloBottomBarScaffold
import com.qello.presentation.component.button.QelloLargeButton
import com.qello.presentation.component.button.QelloSmallButton
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.theme.QelloTheme

@Composable
fun MainScreen(
    onNavigateToQuestionCompose: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateToReceivedQuestion: () -> Unit,
    onNavigateToSentQuestion: () -> Unit,
    onNavigateToMy: () -> Unit,
) {
    QelloBottomBarScaffold(
        selectedTab = HomeBottomBarTab.HOME,
        onTabClick = { tab ->
            when (tab) {
                HomeBottomBarTab.RECEIVED -> onNavigateToReceivedQuestion()
                HomeBottomBarTab.SENT -> onNavigateToSentQuestion()
                HomeBottomBarTab.HOME -> Unit
            }
        },
        onCenterClick = onNavigateToQuestionCompose,
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = QelloTheme.spacing.spacing20),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            QelloText("Main Screen")
    var showQuestionSendSheet by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        QelloText("Main Screen")

        QelloLargeButton(
            text = "질문 보내기",
        ) {
            showQuestionSendSheet = true
        }
        QelloLargeButton(
            text = "알림",
            colors = QelloTheme.buttonColors.darkButtonColors,
        ) {
            onNavigateToNotification()
        }
        QelloSmallButton(
            text = "내게 온 질문",
        ) {
            onNavigateToReceivedQuestion()
        }
        QelloSmallButton(
            text = "내가 보낸 질문",
            colors = QelloTheme.buttonColors.darkButtonColors,
        ) {
            onNavigateToSentQuestion()
        }
        QelloSmallButton(
            text = "마이",
            colors = QelloTheme.buttonColors.lightButtonColors,
        ) {
            onNavigateToMy()
        }
    }

    // 바텀시트
    if (showQuestionSendSheet) {
        ModalBottomSheet(
            onDismissRequest = { showQuestionSendSheet = false },
            containerColor = Color.Transparent,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        QelloTheme.gradient.backgroundStrong,
                        RoundedCornerShape(topStart = QelloTheme.radius.radius24, topEnd = QelloTheme.radius.radius24),
                    )
                    .padding(horizontal = QelloTheme.spacing.spacing20)
                    .padding(vertical = 38.dp),
            ) {
                QelloText(
                    text = "전 세계 사람들에게",
                    style = QelloTheme.typography.heading1,
                    color = QelloTheme.colors.label.strong,
                )
                QelloText(
                    text = "질문을 보내보세요!",
                    style = QelloTheme.typography.heading1,
                    color = QelloTheme.colors.label.strong,
                )

                Spacer(Modifier.height(QelloTheme.spacing.spacing8))

                QelloText(
                    text = "궁금한 것을 질문하고, 다양한 사람들의 답변을 받아보세요.",
                    style = QelloTheme.typography.caption1,
                    color = QelloTheme.colors.label.normal2,
                )

                Spacer(Modifier.height(44.dp))

                QelloLargeButton(
                    text = "질문하러가기",
                ) {
                    showQuestionSendSheet = false
                    onNavigateToQuestionCompose()
                }
            }
        }
    }
}
