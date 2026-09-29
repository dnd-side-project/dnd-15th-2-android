package com.qello.data.repository

import com.qello.data.remote.datasource.SentPostDataSource
import com.qello.data.remote.response.SentPostCardResponse
import com.qello.domain.model.SentPostCard
import com.qello.domain.model.SentPostCursor
import com.qello.domain.model.SentPostDetail
import com.qello.domain.model.SentPostFilter
import com.qello.domain.model.SentPostPage
import com.qello.domain.repository.SentPostRepository
import java.time.Instant
import javax.inject.Inject

// TODO: 질문 보내기로 실제 보낸 질문 데이터가 쌓이면 목데이터 대신 실제 API 응답을 쓰도록 지운다.
private const val USE_MOCK_SENT_POST_DATA = true

class SentPostRepositoryImpl @Inject constructor(
    private val sentPostDataSource: SentPostDataSource,
) : SentPostRepository {
    override suspend fun getSentPosts(filter: SentPostFilter, cursor: SentPostCursor?, limit: Int): SentPostPage {
        if (USE_MOCK_SENT_POST_DATA) return mockSentPostPage(cursor, limit)

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
        if (USE_MOCK_SENT_POST_DATA) return mockSentPostDetail(postId)

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

private val mockSentPostCards: List<SentPostCard> by lazy {
    (1..45).map { index ->
        val hasAnswers = index % 3 != 0
        SentPostCard(
            postId = 9000L + index,
            questionText = "목데이터 질문 $index",
            bodyText = if (index % 2 == 0) "추가로 쓴 본문 $index" else null,
            mediaIds = if (index % 4 == 0) listOf(1L) else emptyList(),
            coarseRegionCode = "한국 서울시",
            submittedAt = Instant.now().minusSeconds(index * 3600L).toString(),
            expiresAt = Instant.now().plusSeconds(86_400L).toString(),
            answerCount = if (hasAnswers) (index % 5 + 1).toLong() else 0L,
            reactionCount = (index % 7).toLong(),
            unreadAnswerCount = if (hasAnswers && index % 2 == 0) 1L else 0L,
        )
    }
}

private fun mockSentPostPage(cursor: SentPostCursor?, limit: Int): SentPostPage {
    val startIndex = if (cursor == null) 0 else mockSentPostCards.indexOfFirst { it.postId == cursor.postId } + 1
    val page = mockSentPostCards.drop(startIndex).take(limit)
    val nextCursor = if (startIndex + limit < mockSentPostCards.size) {
        page.lastOrNull()?.let { SentPostCursor(submittedAt = it.submittedAt, postId = it.postId) }
    } else {
        null
    }
    return SentPostPage(cards = page, nextCursor = nextCursor)
}

private fun mockSentPostDetail(postId: Long): SentPostDetail {
    val card = mockSentPostCards.firstOrNull { it.postId == postId } ?: mockSentPostCards.first()
    return SentPostDetail(card = card, answersReadAt = null)
}
