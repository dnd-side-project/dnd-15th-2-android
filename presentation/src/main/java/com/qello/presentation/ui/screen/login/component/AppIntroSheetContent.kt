package com.qello.presentation.ui.screen.login.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.qello.presentation.R
import com.qello.presentation.component.button.QelloLargeButton
import com.qello.presentation.component.spacer.QelloSpacer
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.designsystem.theme.QelloTheme

@Composable
fun AppIntroSheetContent(
    onNextClick: () -> Unit,
) {
    QelloText(
        text = stringResource(R.string.login_intro_title),
        style = QelloTheme.typography.heading1,
        color = QelloTheme.colors.label.strong,
    )

    QelloSpacer(QelloTheme.spacing.spacing8)

    QelloText(
        text = stringResource(R.string.login_intro_description),
        style = QelloTheme.typography.caption1,
        color = QelloTheme.colors.label.normal2,
    )

    // TODO: 시안의 예시 질문 카드 자리
    QelloSpacer(QelloTheme.spacing.spacing44)

    QelloLargeButton(
        text = stringResource(R.string.login_intro_next),
        onClick = onNextClick
    )
}
