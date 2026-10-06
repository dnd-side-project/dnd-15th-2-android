package com.qello.domain.model

data class SentPostCard(
    val postId: Long,
    val questionText: String,
    val bodyText: String?,
    val media: List<FeedMedia>,
    val coarseRegionCode: String?,
    val submittedAt: String,
    val expiresAt: String,
    val answerCount: Long,
    val reactionCount: Long,
    val unreadAnswerCount: Long,
)
