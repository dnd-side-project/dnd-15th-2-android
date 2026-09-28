package com.qello.data.remote.datasource

import com.qello.data.remote.response.InboxDetailResponse
import com.qello.data.remote.response.InboxListingResponse

interface InboxDataSource {
    suspend fun getInbox(category: String, directionSegmentKey: String?): InboxListingResponse

    suspend fun getDetail(postRecipientId: Long): InboxDetailResponse
}
