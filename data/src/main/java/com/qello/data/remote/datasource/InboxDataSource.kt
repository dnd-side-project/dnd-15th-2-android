package com.qello.data.remote.datasource

import com.qello.data.remote.request.SubmitAnswerRequest
import com.qello.data.remote.response.AnswerSubmissionResponse
import com.qello.data.remote.response.InboxDetailResponse
import com.qello.data.remote.response.InboxListingResponse

interface InboxDataSource {
    suspend fun getInbox(category: String, directionSegmentKey: String?): InboxListingResponse

    suspend fun getDetail(postRecipientId: Long): InboxDetailResponse

    suspend fun submitAnswer(
        postRecipientId: Long,
        idempotencyKey: String,
        request: SubmitAnswerRequest,
    ): AnswerSubmissionResponse

    suspend fun skip(postRecipientId: Long)

    suspend fun revertSkip(postRecipientId: Long)
}
