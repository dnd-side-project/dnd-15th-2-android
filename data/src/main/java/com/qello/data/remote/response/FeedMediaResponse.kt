package com.qello.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class FeedMediaResponse(
    val mediaId: Long,
    val url: String,
    val expiresAt: String,
)
