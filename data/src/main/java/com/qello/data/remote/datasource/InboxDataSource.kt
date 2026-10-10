package com.qello.data.remote.datasource

import com.qello.data.remote.model.inbox.SubmitAnswerRequest
import com.qello.data.remote.model.inbox.AnswerSubmissionResponse
import com.qello.data.remote.model.inbox.InboxDetailResponse
import com.qello.data.remote.model.inbox.InboxListingResponse

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
