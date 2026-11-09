package com.qello.presentation.screen.main.setting.report.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.qello.presentation.R
import com.qello.presentation.designsystem.component.dialog.QelloDialog
import com.qello.presentation.designsystem.component.text.QelloText
import com.qello.presentation.designsystem.theme.QelloTheme
import com.qello.presentation.model.ReportDetailUiModel

@Composable
internal fun ReportDetailDialog(
    report: ReportDetailUiModel,
    onDismiss: () -> Unit,
) {
    QelloDialog(
        title = stringResource(report.status.titleRes()),
        confirmText = stringResource(R.string.report_detail_confirm),
        onConfirmClick = onDismiss,
    ) {
        QelloText(
            text = stringResource(report.status.descriptionRes()),
            style = QelloTheme.typography.caption1.copy(textAlign = TextAlign.Center),
            color = QelloTheme.colors.label.strong,
        )

        Column(
            modifier = Modifier
                .padding(top = QelloTheme.spacing.spacing20)
                .fillMaxWidth()
                .background(
                    // 다이얼로그 그라데이션 위에서 정보 영역만 한 톤 가라앉혀 구분한다
                    color = QelloTheme.colors.background.normalDefault.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(QelloTheme.radius.radius16),
                )
                .padding(QelloTheme.spacing.spacing16),
            verticalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing12),
        ) {
            ReportDetailRow(label = stringResource(R.string.report_detail_status)) {
                Box(
                    modifier = Modifier
                        .size(QelloTheme.spacing.spacing8)
                        .background(report.status.dotColor(), CircleShape),
                )

                QelloText(
                    text = stringResource(report.status.labelRes()),
                    style = QelloTheme.typography.caption1,
                    color = QelloTheme.colors.label.strong,
                )
            }

            ReportDetailRow(
                label = stringResource(R.string.report_detail_reason),
                value = stringResource(report.reasonLabelRes),
            )

            ReportDetailRow(
                label = stringResource(R.string.report_detail_received_at),
                value = report.receivedAtLabel,
            )

            report.resolvedAtLabel?.let { resolvedAt ->
                ReportDetailRow(
                    label = stringResource(R.string.report_detail_resolved_at),
                    value = resolvedAt,
                )
            }
        }
    }
}

@Composable
private fun ReportDetailRow(
    label: String,
    value: String,
) {
    ReportDetailRow(label = label) {
        QelloText(
            text = value,
            style = QelloTheme.typography.caption1,
            color = QelloTheme.colors.label.strong,
        )
    }
}

@Composable
private fun ReportDetailRow(
    label: String,
    value: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        QelloText(
            text = label,
            style = QelloTheme.typography.caption1,
            color = QelloTheme.colors.label.neutral,
            modifier = Modifier.weight(1f),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing6),
            content = value,
        )
    }
}
