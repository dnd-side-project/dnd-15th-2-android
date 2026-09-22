package com.qello.presentation.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.qello.presentation.R
import com.qello.presentation.component.bottombar.HomeBottomBarTab
import com.qello.presentation.component.bottombar.QelloBottomBarScaffold
import com.qello.presentation.component.bottomsheet.QelloBottomSheet
import com.qello.presentation.component.button.QelloIconButton
import com.qello.presentation.component.button.QelloLargeButton
import com.qello.presentation.component.map.QelloMap
import com.qello.presentation.component.spacer.QelloSpacer
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.designsystem.theme.QelloTheme

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToQuestionCompose: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateToReceivedQuestion: () -> Unit,
    onNavigateToSentQuestion: () -> Unit,
    onNavigateToQuestionSuggestList: () -> Unit,
    onNavigateToMy: () -> Unit,
) {
    var showQuestionSendSheet by remember { mutableStateOf(false) }

    QelloBottomBarScaffold(
        selectedTab = HomeBottomBarTab.HOME,
        onTabClick = { tab ->
            when (tab) {
                HomeBottomBarTab.RECEIVED -> onNavigateToReceivedQuestion()
                HomeBottomBarTab.SENT -> onNavigateToSentQuestion()
                HomeBottomBarTab.HOME -> Unit
            }
        },
        onCenterClick = {
            showQuestionSendSheet = true
        },
    ) {
        QelloMap(
            initialCenter = Point.fromLngLat(126.9780, 37.5665),
            initialZoom = 10.0,
            modifier = Modifier.fillMaxSize()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 68.dp, end = 21.dp),
            horizontalArrangement = Arrangement.spacedBy(11.5.dp, Alignment.End)
        ) {
            QelloIconButton(
                painter = painterResource(R.drawable.icon_bell),
            ) {
                onNavigateToNotification()
            }

            QelloIconButton(
                painter = painterResource(R.drawable.icon_hamburgerbar),
            ) {
                onNavigateToQuestionSuggestList()
            }
        }

        // 바텀시트
        if (showQuestionSendSheet) {
            QelloBottomSheet(
                onDismissRequest = { showQuestionSendSheet = false },
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

                QelloSpacer(QelloTheme.spacing.spacing8)

                QelloText(
                    text = "궁금한 것을 질문하고, 다양한 사람들의 답변을 받아보세요.",
                    style = QelloTheme.typography.caption1,
                    color = QelloTheme.colors.label.normal2,
                )

                QelloSpacer(44.dp)

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
