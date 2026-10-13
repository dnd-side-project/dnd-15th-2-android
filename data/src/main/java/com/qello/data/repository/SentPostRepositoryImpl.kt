package com.qello.data.repository

import com.qello.data.mapper.toDomain
import com.qello.data.remote.datasource.SentPostDataSource
import com.qello.domain.model.SentPostCursor
import com.qello.domain.model.SentPostDetail
import com.qello.domain.model.SentPostFilter
import com.qello.domain.model.SentPostPage
import com.qello.domain.repository.SentPostRepository
import javax.inject.Inject

class SentPostRepositoryImpl @Inject constructor(
    private val sentPostDataSource: SentPostDataSource,
) : SentPostRepository {
    override suspend fun getSentPosts(filter: SentPostFilter, cursor: SentPostCursor?, limit: Int): SentPostPage =
        sentPostDataSource.getPosts(
            filter = filter.name,
            cursorSubmittedAt = cursor?.submittedAt,
            cursorPostId = cursor?.postId,
            limit = limit,
        ).toDomain()

    override suspend fun getSentPostDetail(postId: Long): SentPostDetail =
        sentPostDataSource.getDetail(postId).toDomain()
}
