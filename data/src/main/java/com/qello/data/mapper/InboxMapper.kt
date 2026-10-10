package com.qello.data.mapper

import com.qello.data.remote.model.inbox.DirectionChipResponse
import com.qello.data.remote.model.inbox.InboxCardResponse
import com.qello.data.remote.model.inbox.InboxDetailResponse
import com.qello.data.remote.model.inbox.InboxListingResponse
import com.qello.domain.model.DirectionChip
import com.qello.domain.model.InboxCard
import com.qello.domain.model.InboxDetail
import com.qello.domain.model.InboxListing
import com.qello.domain.model.PostRecipientStatus

internal fun InboxCardResponse.toDomain(): InboxCard = InboxCard(
    postRecipientId = postRecipientId,
    postId = postId,
    status = PostRecipientStatus.valueOf(status),
    questionText = questionText,
    bodyText = bodyText,
    media = media.map { it.toDomain() },
    senderCoarseRegionCode = senderCoarseRegionCode,
    inboundBearingDegrees = inboundBearingDegrees,
    distanceM = distanceM,
    distanceBand = distanceBand,
    matchedAt = matchedAt,
    expiresAt = expiresAt,
    answerCount = answerCount,
    reactedByMe = reactedByMe,
    reactionCount = reactionCount,
    unreadAnswerCount = unreadAnswerCount,
)

internal fun DirectionChipResponse.toDomain(): DirectionChip = DirectionChip(
    segmentKey = segmentKey,
    displayName = displayName,
    sortOrder = sortOrder,
    count = count,
)

internal fun InboxListingResponse.toDomain(): InboxListing = InboxListing(
    cards = cards.map { it.toDomain() },
    chips = chips.map { it.toDomain() },
)

internal fun InboxDetailResponse.toDomain(): InboxDetail = InboxDetail(
    card = card.toDomain(),
    openedAt = openedAt,
    skipRequestedAt = skipRequestedAt,
)
