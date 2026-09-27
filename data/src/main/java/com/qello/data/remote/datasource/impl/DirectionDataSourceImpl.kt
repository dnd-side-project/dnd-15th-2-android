package com.qello.data.remote.datasource.impl

import com.qello.data.remote.datasource.DirectionDataSource
import com.qello.data.remote.request.SubmitDirectionPostRequest
import com.qello.data.remote.request.UpdatePresenceRequest
import com.qello.data.remote.response.ApiResponse
import com.qello.data.remote.response.DirectionPostSubmissionResponse
import com.qello.data.remote.response.UpdatePresenceResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
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
}
