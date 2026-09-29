package com.qello.data.remote.datasource.impl

import com.qello.data.remote.datasource.SentPostDataSource
import com.qello.data.remote.response.ApiResponse
import com.qello.data.remote.response.SentPostDetailResponse
import com.qello.data.remote.response.SentPostListingResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class SentPostDataSourceImpl @Inject constructor(
    private val client: HttpClient,
) : SentPostDataSource {
    override suspend fun getPosts(
        filter: String,
        cursorSubmittedAt: String?,
        cursorPostId: Long?,
        limit: Int,
    ): SentPostListingResponse = client.get("direction/posts") {
        parameter("filter", filter)
        cursorSubmittedAt?.let { parameter("cursorSubmittedAt", it) }
        cursorPostId?.let { parameter("cursorPostId", it) }
        parameter("limit", limit)
    }.body<ApiResponse<SentPostListingResponse>>().data

    override suspend fun getDetail(postId: Long): SentPostDetailResponse =
        client.get("direction/posts/$postId").body<ApiResponse<SentPostDetailResponse>>().data
}
