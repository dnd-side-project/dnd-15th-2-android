package com.qello.domain.model

data class ReportReceipt(
    val reportId: Long,
    val status: String,
    val receivedAt: String,
    val alreadyReceived: Boolean,
    val guidance: String,
)
