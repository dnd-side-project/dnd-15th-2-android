package com.qello.presentation.ui.screen.main.question

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
import com.qello.presentation.component.button.QelloSmallButton
import com.qello.presentation.ui.designsystem.theme.QelloTheme

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
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            QelloSmallButton(
                text = primaryButtonText,
                colors = QelloTheme.buttonColors.darkButtonColors,
                onClick = onPrimaryClick,
            )
            QelloSmallButton(
                text = secondaryButtonText,
                onClick = onSecondaryClick,
            )
        }
    }
}
