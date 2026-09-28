package com.qello.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class CreateUploadRequestRequest(
    val byteSize: Long,
    val checksum: String,
    val contentType: String,
)
