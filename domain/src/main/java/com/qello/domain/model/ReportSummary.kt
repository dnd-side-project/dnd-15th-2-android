package com.qello.domain.model

data class ReportSummary(
    val reportId: Long,
    val reason: ReportReason,
    val status: ReportStatus,
    val createdAt: String,
)
