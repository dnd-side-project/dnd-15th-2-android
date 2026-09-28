package com.qello.data.repository

import com.qello.data.remote.datasource.DirectionDataSource
import com.qello.data.remote.request.SubmitDirectionPostRequest
import com.qello.data.remote.request.UpdatePresenceRequest
import com.qello.domain.repository.DirectionRepository
import kotlinx.coroutines.CancellationException
import timber.log.Timber
import java.time.Instant
import javax.inject.Inject

class DirectionRepositoryImpl @Inject constructor(
    private val directionDataSource: DirectionDataSource,
) : DirectionRepository {
    override suspend fun updatePresence(
        latitude: Double,
        longitude: Double,
        accuracyMeters: Double,
        receiveAllowed: Boolean,
    ) {
        // 앱 켤 때의 백그라운드 동기화일 뿐이므로, 실패해도 화면을 막지 않고 조용히 넘어간다
        try {
            directionDataSource.updatePresence(
                UpdatePresenceRequest(
                    latitude = latitude,
                    longitude = longitude,
                    accuracyMeters = accuracyMeters,
                    receiveAllowed = receiveAllowed,
                    observedAt = Instant.now().toString(),
                ),
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Timber.d(e, "방향 위치 갱신에 실패했습니다")
        }
    }

    override suspend fun submitDirectionPost(
        approvedQuestionId: Long,
        schemeId: Long,
        segmentKey: String,
        bodyText: String?,
        mediaIds: List<Long>,
        idempotencyKey: String,
    ) {
        directionDataSource.submitPost(
            idempotencyKey = idempotencyKey,
            request = SubmitDirectionPostRequest(
                approvedQuestionId = approvedQuestionId,
                schemeId = schemeId,
                segmentKey = segmentKey,
                bodyText = bodyText,
                mediaIds = mediaIds,
            ),
        )
    }
}
