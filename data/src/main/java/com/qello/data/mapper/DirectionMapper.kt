package com.qello.data.mapper

import com.qello.data.remote.model.direction.AnswerResponse
import com.qello.data.remote.model.direction.ReactionResponse
import com.qello.data.remote.model.direction.ReportReceiptResponse
import com.qello.domain.model.Answer
import com.qello.domain.model.PostReaction
import com.qello.domain.model.ReportReceipt

internal fun AnswerResponse.toDomain(): Answer = Answer(
    answerId = answerId,
    authorNickname = authorNickname,
    authorCoarseRegionCode = authorCoarseRegionCode,
    bodyText = bodyText,
    media = media.map { it.toDomain() },
    bearingFromSenderDegrees = bearingFromSenderDegrees,
    distanceM = distanceM,
    distanceBand = distanceBand,
    publishedAt = publishedAt,
    editedAt = editedAt,
    reactedByMe = reactedByMe,
    reactionCount = reactionCount,
)

internal fun ReactionResponse.toDomain(): PostReaction =
    PostReaction(reacted = reacted, reactionCount = reactionCount)

internal fun ReportReceiptResponse.toDomain(): ReportReceipt = ReportReceipt(
    reportId = reportId,
    status = status,
    receivedAt = receivedAt,
    alreadyReceived = alreadyReceived,
    guidance = guidance,
)
