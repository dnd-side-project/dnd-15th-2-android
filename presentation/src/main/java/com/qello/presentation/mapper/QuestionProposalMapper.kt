package com.qello.presentation.mapper

import com.qello.domain.model.QuestionProposal
import com.qello.domain.model.QuestionProposalStatus
import com.qello.presentation.model.ProposalStatusUi
import com.qello.presentation.model.QuestionProposalUiModel

internal fun QuestionProposalStatus.toUi(): ProposalStatusUi = when (this) {
    QuestionProposalStatus.DRAFT,
    QuestionProposalStatus.SUBMITTED,
    QuestionProposalStatus.UNDER_REVIEW,
    -> ProposalStatusUi.REVIEWING

    QuestionProposalStatus.APPROVED -> ProposalStatusUi.APPROVED

    QuestionProposalStatus.REJECTED -> ProposalStatusUi.REJECTED

    QuestionProposalStatus.ARCHIVED -> ProposalStatusUi.ARCHIVED
}

internal fun QuestionProposal.toUiModel(): QuestionProposalUiModel = QuestionProposalUiModel(
    id = id,
    proposedText = proposedText,
    status = status.toUi(),
    decisionReason = decisionReason,
    submittedAt = submittedAt,
    dateLabel = createdAt.toDateLabel(),
)
