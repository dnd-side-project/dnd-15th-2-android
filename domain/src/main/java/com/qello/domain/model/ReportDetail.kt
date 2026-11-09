package com.qello.domain.model

data class ReportDetail(
    val reportId: Long,
    val reason: ReportReason,
    val status: ReportStatus,
    val createdAt: String,
    val resolvedAt: String?,
)
