package com.qello.presentation.mapper

import com.qello.domain.model.SentPostCard
import com.qello.presentation.model.SentPostCardUiModel

internal fun SentPostCard.toUiModel(): SentPostCardUiModel = SentPostCardUiModel(
    postId = postId,
    questionText = questionText,
    bodyText = bodyText,
    media = media,
    coarseRegionCode = coarseRegionCode,
    localTimeLabel = submittedAt.toLocalTimeLabel(),
    postedAtLabel = submittedAt.toPostedAtLabel(),
    expiresAt = expiresAt,
    answerCount = answerCount,
    reactionCount = reactionCount,
    unreadAnswerCount = unreadAnswerCount,
)
