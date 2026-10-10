package com.qello.data.repository

import com.qello.data.mapper.toDomain
import com.qello.data.remote.datasource.QuestionDataSource
import com.qello.data.remote.model.question.QuestionProposalRequest
import com.qello.domain.model.QuestionProposal
import com.qello.domain.model.RecommendedQuestion
import com.qello.domain.repository.QuestionRepository
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val questionDataSource: QuestionDataSource,
) : QuestionRepository {
    override suspend fun submitQuestionProposal(proposedText: String) {
        questionDataSource.submitQuestionProposal(QuestionProposalRequest(proposedText = proposedText))
    }

    override suspend fun getMyQuestionProposals(): List<QuestionProposal> =
        questionDataSource.getMyQuestionProposals().map { it.toDomain() }

    override suspend fun getRecommendedQuestions(): List<RecommendedQuestion> =
        questionDataSource.getRecommendedQuestions().map { it.toDomain() }
}
