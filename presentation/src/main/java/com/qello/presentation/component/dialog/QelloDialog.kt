package com.qello.presentation.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.qello.presentation.component.button.QelloSmallButton
import com.qello.presentation.component.spacer.QelloSpacer
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.designsystem.theme.QelloTheme

/**
 * 버튼 두 개를 가지는 Qello 기본 다이얼로그.
 * @param onDismissRequest 뒤로가기 / 다이얼로그 바깥 영역 탭. 기본값은 [onDismissClick]과
 *   동일하며, 두 경로를 다르게 처리해야 할 때만 별도로 넘긴다.
 */
@Composable
fun QelloDialog(
    title: String,
    dismissText: String,
    onDismissClick: () -> Unit,
    confirmText: String,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
    description: String? = null,
    onDismissRequest: () -> Unit = onDismissClick,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        // 기본 플랫폼 폭(고정 280dp 계열)을 끄고 좌우 여백을 직접 제어한다.
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = QelloTheme.spacing.spacing20)
                .background(
                    brush = QelloTheme.gradient.cardHighlight,
                    shape = RoundedCornerShape(QelloTheme.radius.radius24),
                )
                .padding(
                    horizontal = QelloTheme.spacing.spacing20,
                    vertical = QelloTheme.spacing.spacing24,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            QelloText(
                text = title,
                style = QelloTheme.typography.body1,
                color = QelloTheme.colors.label.strong,
            )

            if (description != null) {
                QelloSpacer(QelloTheme.spacing.spacing8)

                QelloText(
                    text = description,
                    style = QelloTheme.typography.caption1,
                    color = QelloTheme.colors.label.strong,
                )
            }

            QelloSpacer(QelloTheme.spacing.spacing24)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing8),
            ) {
                QelloSmallButton(
                    text = dismissText,
                    colors = QelloTheme.buttonColors.darkButtonColors,
                    onClick = onDismissClick,
                    modifier = Modifier.weight(1f),
                )
                QelloSmallButton(
                    text = confirmText,
                    onClick = onConfirmClick,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
