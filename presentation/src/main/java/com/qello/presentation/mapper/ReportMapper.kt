package com.qello.presentation.mapper

import androidx.annotation.StringRes
import com.qello.domain.model.ReportDetail
import com.qello.domain.model.ReportReason
import com.qello.domain.model.ReportStatus
import com.qello.domain.model.ReportSummary
import com.qello.presentation.R
import com.qello.presentation.model.ReportDetailUiModel
import com.qello.presentation.model.ReportStatusUi
import com.qello.presentation.model.ReportUiModel

// 추가 정보 요청(MORE_INFO_REQUIRED)은 앱에 제출 흐름이 없어 사용자가 할 수 있는 일이 없으므로 검토중으로 보여준다.
internal fun ReportStatus.toUi(): ReportStatusUi = when (this) {
    ReportStatus.RECEIVED,
    ReportStatus.AUTO_HIDDEN,
    ReportStatus.UNDER_REVIEW,
    ReportStatus.MORE_INFO_REQUIRED,
    -> ReportStatusUi.REVIEWING

    ReportStatus.ACTIONED -> ReportStatusUi.ACTIONED

    ReportStatus.NO_VIOLATION -> ReportStatusUi.NO_VIOLATION
}

@StringRes
internal fun ReportReason.toLabelRes(): Int = when (this) {
    ReportReason.SEXUAL_CONTENT -> R.string.report_reason_sexual_content
    ReportReason.VIOLENCE_OR_THREAT -> R.string.report_reason_violence_or_threat
    ReportReason.HATE_OR_HARASSMENT -> R.string.report_reason_hate_or_harassment
    ReportReason.PRIVACY_VIOLATION -> R.string.report_reason_privacy_violation
    ReportReason.SPAM_OR_ADVERTISING -> R.string.report_reason_spam_or_advertising
    ReportReason.IMPERSONATION -> R.string.report_reason_impersonation
    ReportReason.ILLEGAL_OR_DANGEROUS -> R.string.report_reason_illegal_or_dangerous
    ReportReason.OTHER -> R.string.report_reason_other
}

internal fun ReportSummary.toUiModel(): ReportUiModel = ReportUiModel(
    id = reportId,
    reasonLabelRes = reason.toLabelRes(),
    status = status.toUi(),
    dateLabel = createdAt.toDateLabel(),
)

internal fun ReportDetail.toUiModel(): ReportDetailUiModel = ReportDetailUiModel(
    id = reportId,
    reasonLabelRes = reason.toLabelRes(),
    status = status.toUi(),
    receivedAtLabel = createdAt.toDateTimeLabel(),
    resolvedAtLabel = resolvedAt?.toDateTimeLabel(),
)
