package com.qello.data.remote.datasource

import com.qello.data.remote.model.question.QuestionProposalResponse
import com.qello.data.remote.model.question.RecommendedQuestionResponse

interface QuestionDataSource {
    suspend fun submitQuestionProposal(proposedText: String): QuestionProposalResponse

    suspend fun getMyQuestionProposals(): List<QuestionProposalResponse>

    suspend fun getRecommendedQuestions(): List<RecommendedQuestionResponse>
}
