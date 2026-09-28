package com.qello.data.remote.datasource.impl

import com.qello.data.remote.datasource.DirectionDataSource
import com.qello.data.remote.request.SubmitDirectionPostRequest
import com.qello.data.remote.request.SubmitReportRequest
import com.qello.data.remote.request.UpdatePresenceRequest
import com.qello.data.remote.response.AnswerListingResponse
import com.qello.data.remote.response.ApiResponse
import com.qello.data.remote.response.DirectionPostSubmissionResponse
import com.qello.data.remote.response.ReactionResponse
import com.qello.data.remote.response.ReportReceiptResponse
import com.qello.data.remote.response.UpdatePresenceResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class DirectionDataSourceImpl @Inject constructor(
    private val client: HttpClient,
) : DirectionDataSource {
    override suspend fun updatePresence(request: UpdatePresenceRequest): UpdatePresenceResponse =
        client.put("direction/presence") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<ApiResponse<UpdatePresenceResponse>>().data

    override suspend fun submitPost(
        idempotencyKey: String,
        request: SubmitDirectionPostRequest,
    ): DirectionPostSubmissionResponse = client.post("direction/posts") {
        contentType(ContentType.Application.Json)
        header("Idempotency-Key", idempotencyKey)
        setBody(request)
    }.body<ApiResponse<DirectionPostSubmissionResponse>>().data

    override suspend fun reactToPost(postId: Long): ReactionResponse =
        client.put("direction/posts/$postId/reaction").body<ApiResponse<ReactionResponse>>().data

    override suspend fun cancelPostReaction(postId: Long): ReactionResponse =
        client.delete("direction/posts/$postId/reaction").body<ApiResponse<ReactionResponse>>().data

    override suspend fun getAnswers(postId: Long, limit: Int): AnswerListingResponse =
        client.get("direction/posts/$postId/answers") {
            parameter("limit", limit)
        }.body<ApiResponse<AnswerListingResponse>>().data

    override suspend fun reactToAnswer(answerId: Long): ReactionResponse =
        client.put("direction/answers/$answerId/reaction").body<ApiResponse<ReactionResponse>>().data

    override suspend fun cancelAnswerReaction(answerId: Long): ReactionResponse =
        client.delete("direction/answers/$answerId/reaction").body<ApiResponse<ReactionResponse>>().data

    override suspend fun reportPost(postId: Long, request: SubmitReportRequest): ReportReceiptResponse =
        client.post("direction-posts/$postId/reports") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<ApiResponse<ReportReceiptResponse>>().data

    override suspend fun reportAnswer(answerId: Long, request: SubmitReportRequest): ReportReceiptResponse =
        client.post("answers/$answerId/reports") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body<ApiResponse<ReportReceiptResponse>>().data
}
