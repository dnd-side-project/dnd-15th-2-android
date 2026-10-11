package com.qello.presentation.mapper

import com.qello.domain.model.InboxCard
import com.qello.presentation.model.InboxCardUiModel

internal fun InboxCard.toUiModel(): InboxCardUiModel = InboxCardUiModel(
    postRecipientId = postRecipientId,
    postId = postId,
    status = status,
    questionText = questionText,
    bodyText = bodyText,
    media = media,
    senderCoarseRegionCode = senderCoarseRegionCode,
    inboundBearingDegrees = inboundBearingDegrees,
    distanceLabel = distanceLabel(distanceBand = distanceBand, distanceM = distanceM),
    localTimeLabel = matchedAt.toLocalTimeLabel(),
    postedAtLabel = matchedAt.toPostedAtLabel(),
    expiresAt = expiresAt,
    answerCount = answerCount,
    reactedByMe = reactedByMe,
    reactionCount = reactionCount,
    unreadAnswerCount = unreadAnswerCount,
)
