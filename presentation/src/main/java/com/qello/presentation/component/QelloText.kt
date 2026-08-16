package com.qello.presentation.component

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.qello.presentation.ui.theme.QelloTheme

@Composable
fun QelloText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = QelloTheme.typography.body1,
    color: Color = QelloTheme.colors.label.normal1,
) {
    BasicText(
        text = text,
        modifier = modifier,
        style = style.copy(color = color),
    )
}
