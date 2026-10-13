package com.qello.data.mapper

import com.qello.data.remote.model.question.QuestionProposalResponse
import com.qello.data.remote.model.question.RecommendedQuestionResponse
import com.qello.domain.model.QuestionProposal
import com.qello.domain.model.QuestionProposalStatus
import com.qello.domain.model.RecommendedQuestion

internal fun QuestionProposalResponse.toDomain(): QuestionProposal = QuestionProposal(
    id = id,
    proposedText = proposedText,
    status = QuestionProposalStatus.valueOf(status),
    decisionReason = decisionReason,
    submittedAt = submittedAt,
    createdAt = createdAt,
)

internal fun RecommendedQuestionResponse.toDomain(): RecommendedQuestion =
    RecommendedQuestion(approvedQuestionId = approvedQuestionId, questionText = questionText)
