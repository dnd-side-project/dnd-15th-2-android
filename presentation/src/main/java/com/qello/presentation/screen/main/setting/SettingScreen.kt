package com.qello.presentation.screen.main.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.qello.presentation.R
import com.qello.presentation.designsystem.component.button.QelloBackButton
import com.qello.presentation.designsystem.component.spacer.QelloSpacer
import com.qello.presentation.designsystem.component.text.QelloText
import com.qello.presentation.designsystem.theme.QelloTheme
import com.qello.presentation.screen.main.setting.component.SettingCard
import com.qello.presentation.screen.main.setting.component.SettingDivider
import com.qello.presentation.screen.main.setting.component.SettingMenuRow
import com.qello.presentation.screen.main.setting.component.SettingProfileRow
import com.qello.presentation.screen.main.setting.component.SettingSectionTitle

@Composable
fun SettingScreen(
    onBack: () -> Unit,
    onNavigateToProfileEdit: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateToQuestionSuggestList: () -> Unit,
    onNavigateToReportHistory: () -> Unit,
    onNavigateToAppInfo: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().background(QelloTheme.gradient.backgroundStrong),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = QelloTheme.spacing.spacing18)
                .padding(vertical = QelloTheme.spacing.spacing36),
        ) {
            QelloBackButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart))

            QelloText(
                text = stringResource(R.string.setting_title),
                style = QelloTheme.typography.heading2,
                color = QelloTheme.colors.label.strong,
                modifier = Modifier.align(Alignment.Center),
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = QelloTheme.spacing.spacing20),
        ) {
            SettingCard {
                SettingProfileRow(
                    nickname = "nickname",
                    caption = stringResource(R.string.setting_profile_edit),
                    onClick = onNavigateToProfileEdit,
                )
            }

            QelloSpacer(QelloTheme.spacing.spacing32)

            SettingSectionTitle(text = stringResource(R.string.setting_section_activity))

            SettingCard {
                SettingMenuRow(
                    label = stringResource(R.string.setting_menu_notification),
                    onClick = onNavigateToNotification,
                )

                SettingDivider()

                SettingMenuRow(
                    label = stringResource(R.string.setting_menu_question_suggest),
                    onClick = onNavigateToQuestionSuggestList,
                )

                SettingDivider()

                SettingMenuRow(
                    label = stringResource(R.string.setting_menu_report_history),
                    onClick = onNavigateToReportHistory,
                )
            }

            QelloSpacer(QelloTheme.spacing.spacing24)

            SettingSectionTitle(text = stringResource(R.string.setting_section_info))

            SettingCard {
                SettingMenuRow(
                    label = stringResource(R.string.setting_menu_app_info),
                    onClick = onNavigateToAppInfo,
                )
            }
        }
    }
}
