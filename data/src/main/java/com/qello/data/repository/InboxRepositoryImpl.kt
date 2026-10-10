package com.qello.data.repository

import com.qello.data.mapper.toDomain
import com.qello.data.remote.datasource.InboxDataSource
import com.qello.data.remote.model.inbox.SubmitAnswerRequest
import com.qello.domain.model.InboxCategory
import com.qello.domain.model.InboxDetail
import com.qello.domain.model.InboxListing
import com.qello.domain.repository.InboxRepository
import javax.inject.Inject

class InboxRepositoryImpl @Inject constructor(
    private val inboxDataSource: InboxDataSource,
) : InboxRepository {
    override suspend fun getInbox(category: InboxCategory, directionSegmentKey: String?): InboxListing =
        inboxDataSource.getInbox(category.name, directionSegmentKey).toDomain()

    override suspend fun getInboxDetail(postRecipientId: Long): InboxDetail =
        inboxDataSource.getDetail(postRecipientId).toDomain()

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

    override suspend fun skipInboxItem(postRecipientId: Long) {
        inboxDataSource.skip(postRecipientId)
    }

    override suspend fun undoSkipInboxItem(postRecipientId: Long) {
        inboxDataSource.revertSkip(postRecipientId)
    }
}
