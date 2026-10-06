package com.qello.domain.model

/** 정확 위치와 내부 사용자 식별자를 제외한, 내가 받은 질문 카드. */
data class InboxCard(
    val postRecipientId: Long,
    val postId: Long,
    val status: PostRecipientStatus,
    val questionText: String,
    val bodyText: String?,
    val media: List<FeedMedia>,
    val senderCoarseRegionCode: String?,
    val inboundBearingDegrees: Double,
    val distanceM: Long?,
    val distanceBand: String?,
    val matchedAt: String,
    val expiresAt: String,
    val answerCount: Long,
    val reactedByMe: Boolean,
    val reactionCount: Long,
    val unreadAnswerCount: Long,
)
