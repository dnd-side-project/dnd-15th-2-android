package com.qello.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class SubmitReportRequest(
    val reasonCode: String,
    val subReasonCode: String? = null,
    val detail: String? = null,
    val blockAuthor: Boolean = false,
)
