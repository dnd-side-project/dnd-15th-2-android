package com.qello.domain.model

/** 질문글에 달린 답변 하나. 정확 좌표와 작성자 내부 식별자는 포함하지 않는다. */
data class Answer(
    val answerId: Long,
    val authorNickname: String,
    val authorCoarseRegionCode: String?,
    val bodyText: String,
    val media: List<FeedMedia>,
    val bearingFromSenderDegrees: Double,
    val distanceM: Long?,
    val distanceBand: String?,
    val publishedAt: String,
    val editedAt: String?,
    val reactedByMe: Boolean,
    val reactionCount: Long,
)
