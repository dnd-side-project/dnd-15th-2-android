package com.qello.presentation.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

            QelloLargeButton(
                text = "질문 보내기",
            ) {
                onNavigateToQuestionCompose()
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
    }
}
