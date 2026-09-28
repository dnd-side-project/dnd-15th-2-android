package com.qello.data.remote.datasource.impl

import com.qello.data.remote.datasource.InboxDataSource
import com.qello.data.remote.request.SubmitAnswerRequest
import com.qello.data.remote.response.AnswerSubmissionResponse
import com.qello.data.remote.response.ApiResponse
import com.qello.data.remote.response.InboxDetailResponse
import com.qello.data.remote.response.InboxListingResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class InboxDataSourceImpl @Inject constructor(
    private val client: HttpClient,
) : InboxDataSource {
    override suspend fun getInbox(category: String, directionSegmentKey: String?): InboxListingResponse =
        client.get("direction/inbox") {
            parameter("category", category)
            directionSegmentKey?.let { parameter("directionSegmentKey", it) }
        }.body<ApiResponse<InboxListingResponse>>().data

    override suspend fun getDetail(postRecipientId: Long): InboxDetailResponse =
        client.get("direction/inbox/$postRecipientId").body<ApiResponse<InboxDetailResponse>>().data

    override suspend fun submitAnswer(
        postRecipientId: Long,
        idempotencyKey: String,
        request: SubmitAnswerRequest,
    ): AnswerSubmissionResponse = client.post("direction/inbox/$postRecipientId/answers") {
        contentType(ContentType.Application.Json)
        header("Idempotency-Key", idempotencyKey)
        setBody(request)
    }.body<ApiResponse<AnswerSubmissionResponse>>().data
}
