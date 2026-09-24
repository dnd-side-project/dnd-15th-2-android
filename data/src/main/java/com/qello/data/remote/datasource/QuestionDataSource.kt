package com.qello.data.remote.datasource

import com.qello.data.remote.response.QuestionProposalResponse

interface QuestionDataSource {
    suspend fun submitQuestionProposal(proposedText: String): QuestionProposalResponse

    suspend fun getMyQuestionProposals(): List<QuestionProposalResponse>
}
