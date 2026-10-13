package com.qello.data.repository

import com.qello.data.mapper.toDomain
import com.qello.data.mapper.toRequest
import com.qello.data.remote.datasource.DirectionDataSource
import com.qello.data.remote.model.direction.SubmitDirectionPostRequest
import com.qello.data.remote.model.direction.UpdatePresenceRequest
import com.qello.domain.model.Answer
import com.qello.domain.model.PostReaction
import com.qello.domain.model.ReportReason
import com.qello.domain.model.ReportReceipt
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

    override suspend fun reactToPost(postId: Long): PostReaction =
        directionDataSource.reactToPost(postId).toDomain()

    override suspend fun cancelPostReaction(postId: Long): PostReaction =
        directionDataSource.cancelPostReaction(postId).toDomain()

    override suspend fun getAnswers(postId: Long, limit: Int): List<Answer> =
        directionDataSource.getAnswers(postId, limit).answers.map { it.toDomain() }

    override suspend fun reactToAnswer(answerId: Long): PostReaction =
        directionDataSource.reactToAnswer(answerId).toDomain()

    override suspend fun cancelAnswerReaction(answerId: Long): PostReaction =
        directionDataSource.cancelAnswerReaction(answerId).toDomain()

    override suspend fun reportPost(
        postId: Long,
        reasonCode: ReportReason,
        detail: String?,
        blockAuthor: Boolean,
    ): ReportReceipt =
        directionDataSource.reportPost(
            postId = postId,
            request = reasonCode.toRequest(detail = detail, blockAuthor = blockAuthor),
        ).toDomain()

    override suspend fun reportAnswer(
        answerId: Long,
        reasonCode: ReportReason,
        detail: String?,
        blockAuthor: Boolean,
    ): ReportReceipt =
        directionDataSource.reportAnswer(
            answerId = answerId,
            request = reasonCode.toRequest(detail = detail, blockAuthor = blockAuthor),
        ).toDomain()

    override suspend fun markAnswersRead(postId: Long) {
        // 배지 해제용 기록일 뿐이므로, 실패해도 화면을 막지 않고 조용히 넘어간다
        try {
            directionDataSource.markAnswersRead(postId)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Timber.d(e, "답변 읽음 처리에 실패했습니다")
        }
    }
}
