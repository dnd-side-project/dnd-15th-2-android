package com.qello.data.repository

import com.qello.data.remote.datasource.QuestionDataSource
import com.qello.data.remote.response.QuestionProposalResponse
import com.qello.domain.model.QuestionProposal
import com.qello.domain.model.QuestionProposalStatus
import com.qello.domain.repository.QuestionRepository
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val questionDataSource: QuestionDataSource,
) : QuestionRepository {
    override suspend fun submitQuestionProposal(proposedText: String) {
        questionDataSource.submitQuestionProposal(proposedText)
    }

    override suspend fun getMyQuestionProposals(): List<QuestionProposal> =
        questionDataSource.getMyQuestionProposals().map { it.toDomain() }

    private fun QuestionProposalResponse.toDomain() = QuestionProposal(
        id = id,
        proposedText = proposedText,
        status = QuestionProposalStatus.valueOf(status),
        decisionReason = decisionReason,
        submittedAt = submittedAt,
        createdAt = createdAt,
    )
}
