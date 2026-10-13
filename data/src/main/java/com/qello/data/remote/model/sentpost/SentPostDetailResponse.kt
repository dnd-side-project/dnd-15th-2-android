package com.qello.data.remote.model.sentpost

import kotlinx.serialization.Serializable

@Serializable
data class SentPostDetailResponse(
    val card: SentPostCardResponse,
    val answersReadAt: String? = null,
)
