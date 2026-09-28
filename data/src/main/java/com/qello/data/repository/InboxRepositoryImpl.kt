package com.qello.data.repository

import com.qello.data.remote.datasource.InboxDataSource
import com.qello.data.remote.request.SubmitAnswerRequest
import com.qello.data.remote.response.DirectionChipResponse
import com.qello.data.remote.response.InboxCardResponse
import com.qello.domain.model.DirectionChip
import com.qello.domain.model.InboxCard
import com.qello.domain.model.InboxCategory
import com.qello.domain.model.InboxDetail
import com.qello.domain.model.InboxListing
import com.qello.domain.model.PostRecipientStatus
import com.qello.domain.repository.InboxRepository
import javax.inject.Inject

class InboxRepositoryImpl @Inject constructor(
    private val inboxDataSource: InboxDataSource,
) : InboxRepository {
    override suspend fun getInbox(category: InboxCategory, directionSegmentKey: String?): InboxListing {
        val response = inboxDataSource.getInbox(category.name, directionSegmentKey)
        return InboxListing(
            cards = response.cards.map { it.toDomain() },
            chips = response.chips.map { it.toDomain() },
        )
    }

    override suspend fun getInboxDetail(postRecipientId: Long): InboxDetail {
        val response = inboxDataSource.getDetail(postRecipientId)
        return InboxDetail(
            card = response.card.toDomain(),
            openedAt = response.openedAt,
            skipRequestedAt = response.skipRequestedAt,
        )
    }

    override suspend fun submitAnswer(
        postRecipientId: Long,
        bodyText: String,
        mediaIds: List<Long>,
        idempotencyKey: String,
    ) {
        inboxDataSource.submitAnswer(
            postRecipientId = postRecipientId,
            idempotencyKey = idempotencyKey,
            request = SubmitAnswerRequest(bodyText = bodyText, mediaIds = mediaIds),
        )
    }

    private fun InboxCardResponse.toDomain() = InboxCard(
        postRecipientId = postRecipientId,
        postId = postId,
        status = PostRecipientStatus.valueOf(status),
        questionText = questionText,
        bodyText = bodyText,
        mediaIds = mediaIds,
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

    private fun DirectionChipResponse.toDomain() = DirectionChip(
        segmentKey = segmentKey,
        displayName = displayName,
        sortOrder = sortOrder,
        count = count,
    )
}
