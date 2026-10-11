package com.qello.presentation.mapper

import com.qello.domain.model.Answer
import com.qello.presentation.model.AnswerUiModel
import java.time.Instant

internal fun Answer.toUiModel(now: Instant = Instant.now()): AnswerUiModel = AnswerUiModel(
    answerId = answerId,
    authorNickname = authorNickname,
    authorCoarseRegionCode = authorCoarseRegionCode,
    bodyText = bodyText,
    media = media,
    relativeTimeLabel = publishedAt.toRelativeTimeLabel(now),
    distanceLabel = distanceLabel(distanceBand = distanceBand, distanceM = distanceM),
    reactedByMe = reactedByMe,
    reactionCount = reactionCount,
)
