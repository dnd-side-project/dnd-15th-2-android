package com.qello.data.remote.datasource

import com.qello.data.remote.response.SentPostDetailResponse
import com.qello.data.remote.response.SentPostListingResponse

interface SentPostDataSource {
    suspend fun getPosts(
        filter: String,
        cursorSubmittedAt: String?,
        cursorPostId: Long?,
        limit: Int,
    ): SentPostListingResponse

    suspend fun getDetail(postId: Long): SentPostDetailResponse
}
