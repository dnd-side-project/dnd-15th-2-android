package com.qello.data.remote.datasource.impl

import com.qello.data.remote.datasource.QuestionDataSource
import com.qello.data.remote.request.QuestionProposalRequest
import com.qello.data.remote.response.ApiResponse
import com.qello.data.remote.response.QuestionProposalResponse
import com.qello.data.remote.response.RecommendedQuestionResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class QuestionDataSourceImpl @Inject constructor(
    private val client: HttpClient,
) : QuestionDataSource {
    override suspend fun submitQuestionProposal(
        proposedText: String,
    ): QuestionProposalResponse = client.post("questions/proposals") {
        contentType(ContentType.Application.Json)
        setBody(QuestionProposalRequest(proposedText = proposedText))
    }.body<ApiResponse<QuestionProposalResponse>>().data

    override suspend fun getMyQuestionProposals(): List<QuestionProposalResponse> =
        client.get("questions/proposals/me").body<ApiResponse<List<QuestionProposalResponse>>>().data

    override suspend fun deleteQuestionProposal(proposalId: Long) {
        client.delete("questions/proposals/$proposalId")
    }

    override suspend fun getRecommendedQuestions(): List<RecommendedQuestionResponse> =
        client.get("questions/recommendations").body<ApiResponse<List<RecommendedQuestionResponse>>>().data
}
