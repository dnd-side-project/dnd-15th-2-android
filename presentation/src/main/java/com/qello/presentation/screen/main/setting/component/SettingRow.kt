package com.qello.presentation.screen.main.setting.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import com.qello.presentation.R
import com.qello.presentation.designsystem.component.text.QelloText
import com.qello.presentation.designsystem.theme.QelloColorPalette
import com.qello.presentation.designsystem.theme.QelloTheme

@Composable
fun SettingProfileRow(
    nickname: String,
    caption: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingClickableRow(onClick = onClick, modifier = modifier) {
        Box(
            modifier = Modifier
                .size(QelloTheme.iconSize.size48)
                .clip(CircleShape)
                .background(QelloTheme.colors.background.normalStrong)
                .border(QelloTheme.borderWidth.border1, QelloTheme.colors.line.normal, CircleShape),
        )

        Spacer(Modifier.width(QelloTheme.spacing.spacing12))

        Column(modifier = Modifier.weight(1f)) {
            QelloText(
                text = nickname,
                style = QelloTheme.typography.body1,
                color = QelloTheme.colors.label.strong,
            )

            QelloText(
                text = caption,
                style = QelloTheme.typography.caption3,
                color = QelloTheme.colors.label.assistive,
                modifier = Modifier.padding(top = QelloTheme.spacing.spacing2),
            )
        }

        SettingChevron()
    }
}

@Composable
fun SettingMenuRow(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingClickableRow(onClick = onClick, modifier = modifier) {
        QelloText(
            text = label,
            style = QelloTheme.typography.body2,
            color = QelloTheme.colors.label.normal1,
            modifier = Modifier.weight(1f),
        )

        SettingChevron()
    }
}

/** ripple 대신 눌린 동안 배경색을 바꿔 터치 피드백을 준다. (QelloProfileImageField와 같은 방식) */
@Composable
private fun SettingClickableRow(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(if (isPressed) QelloColorPalette.Navy30 else Color.Transparent)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Button,
                onClick = onClick,
            )
            .padding(
                horizontal = QelloTheme.spacing.spacing16,
                vertical = QelloTheme.spacing.spacing16,
            ),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

@Composable
private fun SettingChevron() {
    Icon(
        painter = painterResource(R.drawable.ic_right),
        contentDescription = null,
        tint = QelloTheme.colors.label.assistive,
    )
}
