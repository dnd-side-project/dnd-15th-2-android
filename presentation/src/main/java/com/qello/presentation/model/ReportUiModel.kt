package com.qello.presentation.model

import androidx.annotation.StringRes

/** 서버의 6가지 신고 상태를 화면에서 구분하는 3가지로 묶은 것. */
enum class ReportStatusUi {
    REVIEWING,
    ACTIONED,
    NO_VIOLATION,
}

data class ReportUiModel(
    val id: Long,
    @StringRes val reasonLabelRes: Int,
    val status: ReportStatusUi,
    val dateLabel: String,
)

data class ReportDetailUiModel(
    val id: Long,
    @StringRes val reasonLabelRes: Int,
    val status: ReportStatusUi,
    val receivedAtLabel: String,
    val resolvedAtLabel: String?,
)
