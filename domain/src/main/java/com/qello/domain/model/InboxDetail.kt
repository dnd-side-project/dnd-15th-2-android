package com.qello.domain.model

data class InboxDetail(
    val card: InboxCard,
    val openedAt: String,
    val skipRequestedAt: String?,
)
