package com.qello.presentation.model

enum class ProposalStatusUi {
    REVIEWING,
    APPROVED,
    REJECTED,
    ARCHIVED,
}

data class QuestionProposalUiModel(
    val id: Long,
    val proposedText: String,
    val status: ProposalStatusUi,
    val decisionReason: String?,
    val submittedAt: String,
    val dateLabel: String,
)
