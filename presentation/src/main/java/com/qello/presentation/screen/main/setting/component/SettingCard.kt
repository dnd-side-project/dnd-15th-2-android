package com.qello.presentation.screen.main.setting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.qello.presentation.designsystem.component.text.QelloText
import com.qello.presentation.designsystem.theme.QelloColorPalette
import com.qello.presentation.designsystem.theme.QelloTheme

@Composable
fun SettingSectionTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    QelloText(
        text = text,
        style = QelloTheme.typography.caption2,
        color = QelloTheme.colors.label.assistive,
        modifier = modifier.padding(
            start = QelloTheme.spacing.spacing4,
            bottom = QelloTheme.spacing.spacing8,
        ),
    )
}

/** 행들을 하나의 둥근 카드로 묶는다. 안쪽 행의 pressed 배경도 모서리에 맞춰 잘린다. */
@Composable
fun SettingCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(QelloTheme.radius.radius20)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(QelloColorPalette.Navy20)
            .border(QelloTheme.borderWidth.border1, QelloTheme.colors.line.normal, shape),
        content = content,
    )
}

@Composable
fun SettingDivider(
    modifier: Modifier = Modifier,
) {
    HorizontalDivider(
        modifier = modifier.padding(horizontal = QelloTheme.spacing.spacing16),
        thickness = QelloTheme.borderWidth.border1,
        color = QelloTheme.colors.line.normal,
    )
}
