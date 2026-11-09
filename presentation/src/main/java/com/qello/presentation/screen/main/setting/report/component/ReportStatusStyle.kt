package com.qello.presentation.screen.main.setting.report.component

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.qello.presentation.R
import com.qello.presentation.designsystem.theme.QelloTheme
import com.qello.presentation.model.ReportStatusUi

/** 목록 항목과 상세 다이얼로그가 같은 상태를 같은 색, 같은 문구로 보여주도록 한곳에 모은다. */
@Composable
@ReadOnlyComposable
internal fun ReportStatusUi.dotColor(): Color = when (this) {
    ReportStatusUi.REVIEWING -> QelloTheme.colors.primary.normal
    ReportStatusUi.ACTIONED -> QelloTheme.colors.status.positive
    ReportStatusUi.NO_VIOLATION -> QelloTheme.colors.label.assistive
}

@StringRes
internal fun ReportStatusUi.titleRes(): Int = when (this) {
    ReportStatusUi.REVIEWING -> R.string.report_status_reviewing_title
    ReportStatusUi.ACTIONED -> R.string.report_status_actioned_title
    ReportStatusUi.NO_VIOLATION -> R.string.report_status_no_violation_title
}

@StringRes
internal fun ReportStatusUi.labelRes(): Int = when (this) {
    ReportStatusUi.REVIEWING -> R.string.report_status_reviewing_label
    ReportStatusUi.ACTIONED -> R.string.report_status_actioned_label
    ReportStatusUi.NO_VIOLATION -> R.string.report_status_no_violation_label
}

@StringRes
internal fun ReportStatusUi.descriptionRes(): Int = when (this) {
    ReportStatusUi.REVIEWING -> R.string.report_status_reviewing_description
    ReportStatusUi.ACTIONED -> R.string.report_status_actioned_description
    ReportStatusUi.NO_VIOLATION -> R.string.report_status_no_violation_description
}
