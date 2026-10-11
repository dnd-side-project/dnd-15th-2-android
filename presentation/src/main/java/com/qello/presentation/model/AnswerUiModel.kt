package com.qello.presentation.model

import com.qello.domain.model.FeedMedia

data class AnswerUiModel(
    val answerId: Long,
    val authorNickname: String,
    val authorCoarseRegionCode: String?,
    val bodyText: String,
    val media: List<FeedMedia>,
    val bearingFromSenderDegrees: Double,
    val relativeTimeLabel: String,
    val distanceLabel: String,
    val editedAt: String?,
    val reactedByMe: Boolean,
    val reactionCount: Long,
)
