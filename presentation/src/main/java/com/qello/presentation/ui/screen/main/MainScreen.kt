package com.qello.presentation.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.mapbox.geojson.Point
import com.mapbox.maps.extension.compose.MapboxMap
import com.mapbox.maps.extension.compose.animation.viewport.rememberMapViewportState
import com.mapbox.maps.extension.compose.style.BooleanValue
import com.mapbox.maps.extension.compose.style.standard.LightPresetValue
import com.mapbox.maps.extension.compose.style.standard.MapboxStandardStyle
import com.mapbox.maps.extension.compose.style.standard.rememberStandardStyleState
import com.qello.presentation.R
import com.qello.presentation.component.bottombar.HomeBottomBarTab
import com.qello.presentation.component.bottombar.QelloBottomBarScaffold
import com.qello.presentation.component.button.QelloIconButton
import com.qello.presentation.component.button.QelloLargeButton
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.theme.QelloTheme

@Suppress("COMPOSE_APPLIER_CALL_MISMATCH")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToQuestionCompose: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateToReceivedQuestion: () -> Unit,
    onNavigateToSentQuestion: () -> Unit,
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
        MapboxMap(
            modifier = Modifier.fillMaxSize(),
            style = {
                MapboxStandardStyle(
                    standardStyleState = rememberStandardStyleState {
                        configurationsState.apply {
                            lightPreset = LightPresetValue.NIGHT
                            show3dObjects = BooleanValue(false)
                        }
                    },
                )
            },
            mapViewportState = rememberMapViewportState {
                setCameraOptions {
                    center(Point.fromLngLat(126.9780, 37.5665))
                    zoom(10.0)
                }
            },
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
                painter = painterResource(R.drawable.icon_earth),
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
}
