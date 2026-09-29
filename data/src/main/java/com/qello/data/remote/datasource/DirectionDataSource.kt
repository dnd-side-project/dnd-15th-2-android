package com.qello.data.remote.datasource

import com.qello.data.remote.request.SubmitDirectionPostRequest
import com.qello.data.remote.request.SubmitReportRequest
import com.qello.data.remote.request.UpdatePresenceRequest
import com.qello.data.remote.response.AnswerListingResponse
import com.qello.data.remote.response.DirectionPostSubmissionResponse
import com.qello.data.remote.response.ReactionResponse
import com.qello.data.remote.response.ReportReceiptResponse
import com.qello.data.remote.response.UpdatePresenceResponse

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
