package com.qello.domain.model

data class QuestionProposal(
    val id: Long,
    val proposedText: String,
    val status: QuestionProposalStatus,
    val decisionReason: String?,
    val submittedAt: String,
    val createdAt: String,
)
