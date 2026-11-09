package com.qello.data.remote.model.report

import kotlinx.serialization.Serializable

@Serializable
data class ReportPageResponse(
    val items: List<ReportSummaryResponse>,
    val nextCursor: String? = null,
)

@Serializable
data class ReportSummaryResponse(
    val reportId: Long,
    val reasonCode: String,
    val status: String,
    val createdAt: String,
)
