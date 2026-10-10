package com.qello.data.remote.datasource

import com.qello.data.remote.model.sentpost.SentPostDetailResponse
import com.qello.data.remote.model.sentpost.SentPostListingResponse

interface SentPostDataSource {
    suspend fun getPosts(
        filter: String,
        cursorSubmittedAt: String?,
        cursorPostId: Long?,
        limit: Int,
    ): SentPostListingResponse

    suspend fun getDetail(postId: Long): SentPostDetailResponse
}
