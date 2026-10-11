package com.qello.presentation.model

import com.qello.domain.model.FeedMedia

data class SentPostCardUiModel(
    val postId: Long,
    val questionText: String,
    val bodyText: String?,
    val media: List<FeedMedia>,
    val coarseRegionCode: String?,
    val localTimeLabel: String,
    val postedAtLabel: String,
    val expiresAt: String,
    val answerCount: Long,
    val reactionCount: Long,
    val unreadAnswerCount: Long,
)
