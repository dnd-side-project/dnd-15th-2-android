package com.qello.presentation.component.spacer

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.qello.presentation.ui.designsystem.theme.QelloTheme

@Composable
fun QelloSpacer(
    height: Dp = QelloTheme.spacing.spacing12,
) {
    Spacer(modifier = Modifier.height(height))
}
