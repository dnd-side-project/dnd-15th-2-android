package com.qello.data.remote.datasource

import com.qello.data.remote.model.direction.SubmitDirectionPostRequest
import com.qello.data.remote.model.direction.SubmitReportRequest
import com.qello.data.remote.model.direction.UpdatePresenceRequest
import com.qello.data.remote.model.direction.AnswerListingResponse
import com.qello.data.remote.model.direction.DirectionPostSubmissionResponse
import com.qello.data.remote.model.direction.ReactionResponse
import com.qello.data.remote.model.direction.ReportReceiptResponse
import com.qello.data.remote.model.direction.UpdatePresenceResponse

interface DirectionDataSource {
    suspend fun updatePresence(request: UpdatePresenceRequest): UpdatePresenceResponse

    suspend fun submitPost(idempotencyKey: String, request: SubmitDirectionPostRequest): DirectionPostSubmissionResponse

    suspend fun reactToPost(postId: Long): ReactionResponse

    suspend fun cancelPostReaction(postId: Long): ReactionResponse

    suspend fun getAnswers(postId: Long, limit: Int): AnswerListingResponse

    suspend fun reactToAnswer(answerId: Long): ReactionResponse

    suspend fun cancelAnswerReaction(answerId: Long): ReactionResponse

    suspend fun reportPost(postId: Long, request: SubmitReportRequest): ReportReceiptResponse

    suspend fun reportAnswer(answerId: Long, request: SubmitReportRequest): ReportReceiptResponse

    suspend fun markAnswersRead(postId: Long)
}
