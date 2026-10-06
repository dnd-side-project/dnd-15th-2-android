package com.qello.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class AnswerListingResponse(
    val answers: List<AnswerResponse>,
    val nextCursor: AnswerCursorResponse? = null,
)

@Serializable
data class AnswerResponse(
    val answerId: Long,
    val authorNickname: String,
    val authorCoarseRegionCode: String? = null,
    val bodyText: String,
    val media: List<FeedMediaResponse> = emptyList(),
    val bearingFromSenderDegrees: Double,
    val distanceM: Long? = null,
    val distanceBand: String? = null,
    val publishedAt: String,
    val editedAt: String? = null,
    val reactedByMe: Boolean,
    val reactionCount: Long,
)

@Serializable
data class AnswerCursorResponse(
    val publishedAt: String,
    val answerId: Long,
)
