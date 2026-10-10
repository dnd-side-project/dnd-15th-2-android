package com.qello.data.remote.datasource.impl

import com.qello.data.remote.datasource.QuestionDataSource
import com.qello.data.remote.model.question.QuestionProposalRequest
import com.qello.data.remote.model.common.ApiResponse
import com.qello.data.remote.model.question.QuestionProposalResponse
import com.qello.data.remote.model.question.RecommendedQuestionResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
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
        request: QuestionProposalRequest,
    ): QuestionProposalResponse = client.post("questions/proposals") {
        contentType(ContentType.Application.Json)
        setBody(request)
    }.body<ApiResponse<QuestionProposalResponse>>().data

    override suspend fun getMyQuestionProposals(): List<QuestionProposalResponse> =
        client.get("questions/proposals/me").body<ApiResponse<List<QuestionProposalResponse>>>().data

    override suspend fun getRecommendedQuestions(): List<RecommendedQuestionResponse> =
        client.get("questions/recommendations").body<ApiResponse<List<RecommendedQuestionResponse>>>().data
}
