package com.qello.data.remote.datasource

import com.qello.data.remote.model.question.QuestionProposalRequest
import com.qello.data.remote.model.question.QuestionProposalResponse
import com.qello.data.remote.model.question.RecommendedQuestionResponse

interface QuestionDataSource {
    suspend fun submitQuestionProposal(request: QuestionProposalRequest): QuestionProposalResponse

    suspend fun getMyQuestionProposals(): List<QuestionProposalResponse>

    suspend fun deleteQuestionProposal(proposalId: Long)

    suspend fun getRecommendedQuestions(): List<RecommendedQuestionResponse>
}
