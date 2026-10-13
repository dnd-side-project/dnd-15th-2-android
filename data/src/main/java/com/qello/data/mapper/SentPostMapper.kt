package com.qello.data.mapper

import com.qello.data.remote.model.sentpost.SentPostCardResponse
import com.qello.data.remote.model.sentpost.SentPostCursorResponse
import com.qello.data.remote.model.sentpost.SentPostDetailResponse
import com.qello.data.remote.model.sentpost.SentPostListingResponse
import com.qello.domain.model.SentPostCard
import com.qello.domain.model.SentPostCursor
import com.qello.domain.model.SentPostDetail
import com.qello.domain.model.SentPostPage

internal fun SentPostCardResponse.toDomain(): SentPostCard = SentPostCard(
    postId = postId,
    questionText = questionText,
    bodyText = bodyText,
    media = media.map { it.toDomain() },
    coarseRegionCode = coarseRegionCode,
    submittedAt = submittedAt,
    expiresAt = expiresAt,
    answerCount = answerCount,
    reactionCount = reactionCount,
    unreadAnswerCount = unreadAnswerCount,
)

internal fun SentPostCursorResponse.toDomain(): SentPostCursor =
    SentPostCursor(submittedAt = submittedAt, postId = postId)

internal fun SentPostListingResponse.toDomain(): SentPostPage = SentPostPage(
    cards = cards.map { it.toDomain() },
    nextCursor = nextCursor?.toDomain(),
)

internal fun SentPostDetailResponse.toDomain(): SentPostDetail =
    SentPostDetail(card = card.toDomain(), answersReadAt = answersReadAt)
