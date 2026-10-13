package com.qello.data.remote.model.media

import kotlinx.serialization.Serializable

@Serializable
data class CreateUploadRequestRequest(
    val byteSize: Long,
    val checksum: String,
    val contentType: String,
)
