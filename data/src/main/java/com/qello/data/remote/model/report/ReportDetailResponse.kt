package com.qello.data.remote.model.report

import kotlinx.serialization.Serializable

@Serializable
data class ReportDetailResponse(
    val reportId: Long,
    val reasonCode: String,
    val subReasonCode: String? = null,
    val detail: String? = null,
    val status: String,
    val createdAt: String,
    val resolvedAt: String? = null,
)
