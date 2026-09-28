package com.qello.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class InboxDetailResponse(
    val card: InboxCardResponse,
    val openedAt: String,
    val skipRequestedAt: String? = null,
)
