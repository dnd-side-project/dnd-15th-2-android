package com.qello.domain.repository

import com.qello.domain.model.QuestionProposal
import com.qello.domain.model.RecommendedQuestion

interface QuestionRepository {
    suspend fun submitQuestionProposal(proposedText: String)

    suspend fun getMyQuestionProposals(): List<QuestionProposal>

    suspend fun deleteQuestionProposal(proposalId: Long)

    /** 질문 보내기 화면에서 고를 수 있는 질문 목록. 받은 approvedQuestionId를 질문 전송 요청에 그대로 쓴다. */
    suspend fun getRecommendedQuestions(): List<RecommendedQuestion>
}
