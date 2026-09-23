package com.qello.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class QuestionProposalResponse(
    val id: Long,
    val proposedText: String,
    val proposerId: Long,
    val status: String,
    val decisionReason: String? = null,
    val submittedAt: String,
    val createdAt: String,
    val updatedAt: String,
)
