package com.qello.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class InboxListingResponse(
    val cards: List<InboxCardResponse>,
    val chips: List<DirectionChipResponse>,
)

@Serializable
data class InboxCardResponse(
    val postRecipientId: Long,
    val postId: Long,
    val status: String,
    val questionText: String,
    val bodyText: String? = null,
    val media: List<FeedMediaResponse> = emptyList(),
    val senderCoarseRegionCode: String? = null,
    val inboundBearingDegrees: Double,
    val distanceM: Long? = null,
    val distanceBand: String? = null,
    val matchedAt: String,
    val expiresAt: String,
    val answerCount: Long,
    val reactedByMe: Boolean,
    val reactionCount: Long,
    val unreadAnswerCount: Long,
)

@Serializable
data class DirectionChipResponse(
    val segmentKey: String,
    val displayName: String,
    val sortOrder: Int,
    val count: Long,
)
