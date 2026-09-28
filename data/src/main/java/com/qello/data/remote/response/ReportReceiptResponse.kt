package com.qello.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class ReportReceiptResponse(
    val reportId: Long,
    val status: String,
    val receivedAt: String,
    val alreadyReceived: Boolean,
    val guidance: String,
)
