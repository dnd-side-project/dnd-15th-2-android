package com.qello.presentation.model

import com.qello.domain.model.FeedMedia
import com.qello.domain.model.PostRecipientStatus

data class InboxCardUiModel(
    val postRecipientId: Long,
    val postId: Long,
    val status: PostRecipientStatus,
    val questionText: String,
    val bodyText: String?,
    val media: List<FeedMedia>,
    val senderCoarseRegionCode: String?,
    val inboundBearingDegrees: Double,
    val distanceLabel: String,
    val localTimeLabel: String,
    val postedAtLabel: String,
    val expiresAt: String,
    val answerCount: Long,
    val reactedByMe: Boolean,
    val reactionCount: Long,
    val unreadAnswerCount: Long,
)
