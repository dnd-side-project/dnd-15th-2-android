package com.qello.presentation.screen.main.setting.report

import androidx.annotation.StringRes
import com.qello.presentation.R
import com.qello.presentation.model.ReportDetailUiModel
import com.qello.presentation.model.ReportStatusUi
import com.qello.presentation.model.ReportUiModel

enum class ReportHistoryTab(@StringRes val labelRes: Int) {
    ALL(R.string.report_history_tab_all),
    REVIEWING(R.string.report_history_tab_reviewing),
    COMPLETED(R.string.report_history_tab_completed),
}

data class ReportHistoryUiState(
    val reports: List<ReportUiModel> = emptyList(),
    val selectedTab: ReportHistoryTab = ReportHistoryTab.ALL,
    val selectedReport: ReportDetailUiModel? = null,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
) {
    val visibleReports: List<ReportUiModel>
        get() = when (selectedTab) {
            ReportHistoryTab.ALL -> reports
            ReportHistoryTab.REVIEWING -> reports.filter { it.status == ReportStatusUi.REVIEWING }
            ReportHistoryTab.COMPLETED -> reports.filter { it.status != ReportStatusUi.REVIEWING }
        }
}
