package com.qello.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class SentPostListingResponse(
    val cards: List<SentPostCardResponse>,
    val nextCursor: SentPostCursorResponse? = null,
)

@Serializable
data class SentPostCardResponse(
    val postId: Long,
    val questionText: String,
    val bodyText: String? = null,
    val media: List<FeedMediaResponse> = emptyList(),
    val coarseRegionCode: String? = null,
    val submittedAt: String,
    val expiresAt: String,
    val answerCount: Long,
    val reactionCount: Long,
    val unreadAnswerCount: Long,
)

@Serializable
data class SentPostCursorResponse(
    val submittedAt: String,
    val postId: Long,
)
