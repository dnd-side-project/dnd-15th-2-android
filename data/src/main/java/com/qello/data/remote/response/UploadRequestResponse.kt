package com.qello.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class UploadRequestResponse(
    val mediaId: Long,
    val uploadUrl: String,
    val expiresAt: String,
)
