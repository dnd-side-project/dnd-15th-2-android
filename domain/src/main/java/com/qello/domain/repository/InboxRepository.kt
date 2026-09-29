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

    /** 내가 받은 질문에 답변한다. 한 질문에는 한 번만 답할 수 있다. */
    suspend fun submitAnswer(
        postRecipientId: Long,
        bodyText: String,
        mediaIds: List<Long> = emptyList(),
        idempotencyKey: String,
    )

    /** 수신함에서 "글 숨기기": 해당 항목을 넘김 처리해 목록에서 사라지게 한다. */
    suspend fun skipInboxItem(postRecipientId: Long)

    /** "글 숨기기"를 실행취소한다. 서버가 정한 유예 마감이 지나면 실패한다. */
    suspend fun undoSkipInboxItem(postRecipientId: Long)
}
