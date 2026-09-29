package com.qello.data.repository

import com.qello.data.remote.datasource.SentPostDataSource
import com.qello.data.remote.response.SentPostCardResponse
import com.qello.domain.model.SentPostCard
import com.qello.domain.model.SentPostCursor
import com.qello.domain.model.SentPostDetail
import com.qello.domain.model.SentPostFilter
import com.qello.domain.model.SentPostPage
import com.qello.domain.repository.SentPostRepository
import javax.inject.Inject

class SentPostRepositoryImpl @Inject constructor(
    private val sentPostDataSource: SentPostDataSource,
) : SentPostRepository {
    override suspend fun getSentPosts(filter: SentPostFilter, cursor: SentPostCursor?, limit: Int): SentPostPage {
        val response = sentPostDataSource.getPosts(
            filter = filter.name,
            cursorSubmittedAt = cursor?.submittedAt,
            cursorPostId = cursor?.postId,
            limit = limit,
        )
        return SentPostPage(
            cards = response.cards.map { it.toDomain() },
            nextCursor = response.nextCursor?.let { SentPostCursor(submittedAt = it.submittedAt, postId = it.postId) },
        )
    }

    override suspend fun getSentPostDetail(postId: Long): SentPostDetail {
        val response = sentPostDataSource.getDetail(postId)
        return SentPostDetail(card = response.card.toDomain(), answersReadAt = response.answersReadAt)
    }

    private fun SentPostCardResponse.toDomain() = SentPostCard(
        postId = postId,
        questionText = questionText,
        bodyText = bodyText,
        mediaIds = mediaIds,
        coarseRegionCode = coarseRegionCode,
        submittedAt = submittedAt,
        expiresAt = expiresAt,
        answerCount = answerCount,
        reactionCount = reactionCount,
        unreadAnswerCount = unreadAnswerCount,
    )
}
