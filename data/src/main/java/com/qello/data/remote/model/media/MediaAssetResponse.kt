package com.qello.data.remote.model.media

import kotlinx.serialization.Serializable

@Serializable
data class MediaAssetResponse(
    val mediaId: Long,
    val status: String,
)
