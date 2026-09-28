package com.qello.domain.repository

interface DirectionRepository {
    suspend fun updatePresence(
        latitude: Double,
        longitude: Double,
        accuracyMeters: Double,
        receiveAllowed: Boolean = true,
    )

    /** 고른 방향에 있는 사람들에게 질문글을 보낸다. */
    suspend fun submitDirectionPost(
        approvedQuestionId: Long,
        schemeId: Long,
        segmentKey: String,
        bodyText: String?,
        mediaIds: List<Long>,
        idempotencyKey: String,
    )
}
