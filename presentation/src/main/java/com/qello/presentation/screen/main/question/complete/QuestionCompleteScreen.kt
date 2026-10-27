package com.qello.presentation.screen.main.question.complete

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.qello.presentation.designsystem.component.button.QelloSmallButton
import com.qello.presentation.designsystem.theme.QelloTheme
import com.qello.presentation.screen.main.question.component.QuestionStatusHeader
import com.qello.presentation.screen.main.question.component.QuestionStatusImage

@Composable
fun QuestionCompleteScreen(
    titleLine1: String,
    titleLine2: String,
    caption: String,
    @DrawableRes imageRes: Int,
    primaryButtonText: String,
    secondaryButtonText: String,
    onPrimaryClick: () -> Unit,
    onSecondaryClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QelloTheme.gradient.backgroundStrong)
            .padding(horizontal = QelloTheme.spacing.spacing20),
    ) {
        QuestionStatusHeader(titleLine1 = titleLine1, titleLine2 = titleLine2, caption = caption)

        Spacer(Modifier.weight(1f))

        QuestionStatusImage(modifier = Modifier.align(Alignment.CenterHorizontally), imageRes = imageRes)

        Spacer(Modifier.weight(1f))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = QelloTheme.spacing.spacing24),
            horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing8),
        ) {
            QelloSmallButton(
                text = primaryButtonText,
                modifier = Modifier.weight(1f),
                colors = QelloTheme.buttonColors.darkButtonColors,
                onClick = onPrimaryClick,
            )

            QelloSmallButton(
                text = secondaryButtonText,
                modifier = Modifier.weight(1f),
                onClick = onSecondaryClick,
            )
        }
    }
}
