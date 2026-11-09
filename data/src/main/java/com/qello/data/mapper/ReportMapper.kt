package com.qello.data.mapper

import com.qello.data.remote.model.report.ReportDetailResponse
import com.qello.data.remote.model.report.ReportPageResponse
import com.qello.data.remote.model.report.ReportSummaryResponse
import com.qello.domain.model.ReportDetail
import com.qello.domain.model.ReportPage
import com.qello.domain.model.ReportReason
import com.qello.domain.model.ReportStatus
import com.qello.domain.model.ReportSummary

internal fun ReportSummaryResponse.toDomain(): ReportSummary = ReportSummary(
    reportId = reportId,
    reason = ReportReason.valueOf(reasonCode),
    status = ReportStatus.valueOf(status),
    createdAt = createdAt,
)

internal fun ReportPageResponse.toDomain(): ReportPage = ReportPage(
    reports = items.map { it.toDomain() },
    nextCursor = nextCursor,
)

internal fun ReportDetailResponse.toDomain(): ReportDetail = ReportDetail(
    reportId = reportId,
    reason = ReportReason.valueOf(reasonCode),
    status = ReportStatus.valueOf(status),
    createdAt = createdAt,
    resolvedAt = resolvedAt,
)
