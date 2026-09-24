package com.qello.domain.repository

import com.qello.domain.model.QuestionProposal

interface QuestionRepository {
    suspend fun submitQuestionProposal(proposedText: String)

    suspend fun getMyQuestionProposals(): List<QuestionProposal>
}
