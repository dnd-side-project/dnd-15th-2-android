package com.qello.presentation.ui.screen.login.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessResumed
import com.qello.presentation.R
import com.qello.presentation.component.button.QelloLargeButton
import com.qello.presentation.component.spacer.QelloSpacer
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.designsystem.theme.QelloTheme

@Composable
fun WelcomeScreen(
    nickname: String,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(QelloTheme.gradient.backgroundStrong)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = QelloTheme.spacing.spacing20),
        ) {
            QelloSpacer(130.dp)

            QelloText(
                text = stringResource(R.string.welcome_title_nickname, nickname),
                style = QelloTheme.typography.heading1,
                color = QelloTheme.colors.label.strong,
            )

            QelloText(
                text = stringResource(R.string.welcome_title_greeting),
                style = QelloTheme.typography.heading1,
                color = QelloTheme.colors.label.strong,
            )

            QelloSpacer(QelloTheme.spacing.spacing8)

            QelloText(
                text = stringResource(R.string.welcome_description),
                style = QelloTheme.typography.caption1,
                color = QelloTheme.colors.label.normal2,
            )

            Image(
                painter = painterResource(R.drawable.img_fanfare),
                contentDescription = null,
                modifier = Modifier.size(412.dp, 614.dp),
            )
        }

        QelloLargeButton(
            text = stringResource(R.string.welcome_start),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = QelloTheme.spacing.spacing20)
                .padding(bottom = QelloTheme.spacing.spacing24),
            onClick = dropUnlessResumed { onStartClick() },
        )
    }
}
