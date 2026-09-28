package com.qello.data.repository

import com.qello.data.remote.datasource.InboxDataSource
import com.qello.data.remote.response.DirectionChipResponse
import com.qello.data.remote.response.InboxCardResponse
import com.qello.domain.model.DirectionChip
import com.qello.domain.model.InboxCard
import com.qello.domain.model.InboxCategory
import com.qello.domain.model.InboxDetail
import com.qello.domain.model.InboxListing
import com.qello.domain.model.PostRecipientStatus
import com.qello.domain.repository.InboxRepository
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class InboxRepositoryImpl @Inject constructor(
    private val inboxDataSource: InboxDataSource,
) : InboxRepository {
    override suspend fun getInbox(category: InboxCategory, directionSegmentKey: String?): InboxListing {
        // TODO: 실제로 받은 질문 데이터가 쌓이면 이 목데이터 폴백을 지운다
        if (USE_MOCK_INBOX_DATA) {
            return mockInboxListing(directionSegmentKey)
        }

        val response = inboxDataSource.getInbox(category.name, directionSegmentKey)
        return InboxListing(
            cards = response.cards.map { it.toDomain() },
            chips = response.chips.map { it.toDomain() },
        )
    }

    override suspend fun getInboxDetail(postRecipientId: Long): InboxDetail {
        // TODO: 실제로 받은 질문 데이터가 쌓이면 이 목데이터 폴백을 지운다
        if (USE_MOCK_INBOX_DATA) {
            return mockInboxDetail(postRecipientId)
        }

        val response = inboxDataSource.getDetail(postRecipientId)
        return InboxDetail(
            card = response.card.toDomain(),
            openedAt = response.openedAt,
            skipRequestedAt = response.skipRequestedAt,
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

    private fun mockInboxListing(directionSegmentKey: String?): InboxListing {
        val now = Instant.now()
        val cards = listOf(
            InboxCard(
                postRecipientId = 1L,
                postId = 101L,
                status = PostRecipientStatus.DISCOVERED,
                questionText = "다들 어떤 스포츠 좋아하시나요?",
                bodyText = "저는 요즘 클라이밍에 빠졌어요!",
                mediaIds = listOf(1L),
                senderCoarseRegionCode = "KR-11",
                inboundBearingDegrees = 0.0,
                distanceM = 1_200L,
                distanceBand = null,
                matchedAt = now.minus(30, ChronoUnit.MINUTES).toString(),
                expiresAt = now.plus(1, ChronoUnit.DAYS).toString(),
                answerCount = 3,
                reactedByMe = false,
                reactionCount = 5,
                unreadAnswerCount = 1,
            ),
            InboxCard(
                postRecipientId = 2L,
                postId = 102L,
                status = PostRecipientStatus.ANSWERED,
                questionText = "요즘 제일 자주 듣는 노래는요?",
                bodyText = null,
                mediaIds = emptyList(),
                senderCoarseRegionCode = "KR-26",
                inboundBearingDegrees = 90.0,
                distanceM = null,
                distanceBand = "가까운 거리",
                matchedAt = now.minus(2, ChronoUnit.HOURS).toString(),
                expiresAt = now.plus(1, ChronoUnit.DAYS).toString(),
                answerCount = 7,
                reactedByMe = true,
                reactionCount = 12,
                unreadAnswerCount = 0,
            ),
            InboxCard(
                postRecipientId = 3L,
                postId = 103L,
                status = PostRecipientStatus.DISCOVERED,
                questionText = "주말엔 보통 뭐 하시나요?",
                bodyText = "저는 등산 가요",
                mediaIds = listOf(2L),
                senderCoarseRegionCode = "KR-48",
                inboundBearingDegrees = 225.0,
                distanceM = 8_500L,
                distanceBand = null,
                matchedAt = now.minus(5, ChronoUnit.MINUTES).toString(),
                expiresAt = now.plus(1, ChronoUnit.DAYS).toString(),
                answerCount = 0,
                reactedByMe = false,
                reactionCount = 1,
                unreadAnswerCount = 0,
            ),
        )
        val chips = listOf(
            DirectionChip(segmentKey = "N", displayName = "북", sortOrder = 0, count = 1),
            DirectionChip(segmentKey = "E", displayName = "동", sortOrder = 2, count = 1),
            DirectionChip(segmentKey = "SW", displayName = "남서", sortOrder = 5, count = 1),
        )

        val filteredCards = if (directionSegmentKey == null) {
            cards
        } else {
            cards.filter { it.toMockSegmentKey() == directionSegmentKey }
        }
        return InboxListing(cards = filteredCards, chips = chips)
    }

    private fun mockInboxDetail(postRecipientId: Long): InboxDetail {
        val cards = mockInboxListing(directionSegmentKey = null).cards
        val card = cards.firstOrNull { it.postRecipientId == postRecipientId } ?: cards.first()
        return InboxDetail(
            card = card,
            openedAt = Instant.now().toString(),
            skipRequestedAt = null,
        )
    }

    // 목데이터에서만 쓰는 간이 매핑. 실제 CompassDirection과 같은 45도 8방향 규칙이다.
    private fun InboxCard.toMockSegmentKey(): String {
        val segmentKeys = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
        val normalized = ((inboundBearingDegrees % 360.0) + 360.0) % 360.0
        val index = ((normalized + 22.5) / 45.0).toInt() % segmentKeys.size
        return segmentKeys[index]
    }

    private companion object {
        const val USE_MOCK_INBOX_DATA = true
    }
}
