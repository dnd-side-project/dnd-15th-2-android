package com.qello.domain.repository

import com.qello.domain.model.InboxCategory
import com.qello.domain.model.InboxDetail
import com.qello.domain.model.InboxListing

interface InboxRepository {
    suspend fun getInbox(
        category: InboxCategory,
        directionSegmentKey: String? = null,
    ): InboxListing

    /** 수신함 항목을 조회하고, 서버에 최초 열람 상태를 기록한다. */
    suspend fun getInboxDetail(postRecipientId: Long): InboxDetail
}
