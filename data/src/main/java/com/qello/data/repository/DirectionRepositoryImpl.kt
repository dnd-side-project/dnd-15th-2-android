package com.qello.data.repository

import com.qello.data.remote.datasource.DirectionDataSource
import com.qello.data.remote.request.SubmitDirectionPostRequest
import com.qello.data.remote.request.SubmitReportRequest
import com.qello.data.remote.request.UpdatePresenceRequest
import com.qello.data.remote.response.AnswerResponse
import com.qello.data.remote.response.ReportReceiptResponse
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

    override suspend fun reactToPost(postId: Long): PostReaction {
        val response = directionDataSource.reactToPost(postId)
        return PostReaction(reacted = response.reacted, reactionCount = response.reactionCount)
    }

    override suspend fun cancelPostReaction(postId: Long): PostReaction {
        val response = directionDataSource.cancelPostReaction(postId)
        return PostReaction(reacted = response.reacted, reactionCount = response.reactionCount)
    }

    override suspend fun getAnswers(postId: Long, limit: Int): List<Answer> =
        directionDataSource.getAnswers(postId, limit).answers.map { it.toDomain() }

    override suspend fun reactToAnswer(answerId: Long): PostReaction {
        val response = directionDataSource.reactToAnswer(answerId)
        return PostReaction(reacted = response.reacted, reactionCount = response.reactionCount)
    }

    override suspend fun cancelAnswerReaction(answerId: Long): PostReaction {
        val response = directionDataSource.cancelAnswerReaction(answerId)
        return PostReaction(reacted = response.reacted, reactionCount = response.reactionCount)
    }

    override suspend fun reportPost(
        postId: Long,
        reasonCode: ReportReason,
        detail: String?,
        blockAuthor: Boolean,
    ): ReportReceipt =
        directionDataSource.reportPost(
            postId = postId,
            request = SubmitReportRequest(reasonCode = reasonCode.name, detail = detail, blockAuthor = blockAuthor),
        ).toDomain()

    override suspend fun reportAnswer(
        answerId: Long,
        reasonCode: ReportReason,
        detail: String?,
        blockAuthor: Boolean,
    ): ReportReceipt =
        directionDataSource.reportAnswer(
            answerId = answerId,
            request = SubmitReportRequest(reasonCode = reasonCode.name, detail = detail, blockAuthor = blockAuthor),
        ).toDomain()

    private fun ReportReceiptResponse.toDomain() = ReportReceipt(
        reportId = reportId,
        status = status,
        receivedAt = receivedAt,
        alreadyReceived = alreadyReceived,
        guidance = guidance,
    )

    private fun AnswerResponse.toDomain() = Answer(
        answerId = answerId,
        authorNickname = authorNickname,
        authorCoarseRegionCode = authorCoarseRegionCode,
        bodyText = bodyText,
        mediaIds = mediaIds,
        bearingFromSenderDegrees = bearingFromSenderDegrees,
        distanceM = distanceM,
        distanceBand = distanceBand,
        publishedAt = publishedAt,
        editedAt = editedAt,
        reactedByMe = reactedByMe,
        reactionCount = reactionCount,
    )
}
