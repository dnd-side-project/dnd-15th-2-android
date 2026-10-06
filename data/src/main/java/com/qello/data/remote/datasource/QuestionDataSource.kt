package com.qello.data.remote.datasource

import com.qello.data.remote.response.QuestionProposalResponse
import com.qello.data.remote.response.RecommendedQuestionResponse

interface QuestionDataSource {
    suspend fun submitQuestionProposal(proposedText: String): QuestionProposalResponse

    suspend fun getMyQuestionProposals(): List<QuestionProposalResponse>

    suspend fun getRecommendedQuestions(): List<RecommendedQuestionResponse>
}
