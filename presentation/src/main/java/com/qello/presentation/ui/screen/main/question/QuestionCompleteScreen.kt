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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.qello.presentation.R
import com.qello.presentation.component.button.QelloSmallButton
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.theme.QelloTheme

@Composable
fun QuestionCompleteScreen(
    titleLine1: String,
    titleLine2: String,
    caption: String,
    primaryButtonText: String,
    onSendAnother: () -> Unit,
    onNavigateHome: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QelloTheme.gradient.backgroundStrong)
            .padding(horizontal = QelloTheme.spacing.spacing20),
    ) {
        Spacer(Modifier.height(24.dp + QelloTheme.iconSize.size48 + 57.dp))

        Column(modifier = Modifier.padding(horizontal = QelloTheme.spacing.spacing8)) {
            QelloText(
                text = titleLine1,
                style = QelloTheme.typography.heading1,
                color = QelloTheme.colors.label.strong,
            )

            QelloText(
                text = titleLine2,
                style = QelloTheme.typography.heading1,
                color = QelloTheme.colors.label.strong,
            )

            Spacer(Modifier.height(QelloTheme.spacing.spacing8))

            QelloText(
                text = caption,
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
                text = stringResource(R.string.navigate_home_button),
                onClick = onNavigateHome,
            )
        }
    }
}
