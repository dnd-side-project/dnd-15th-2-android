package com.qello.data.remote.datasource.impl

import com.qello.data.di.RawHttpClient
import com.qello.data.remote.datasource.MediaDataSource
import com.qello.data.remote.model.media.CreateUploadRequestRequest
import com.qello.data.remote.model.common.ApiResponse
import com.qello.data.remote.model.media.MediaAssetResponse
import com.qello.data.remote.model.media.UploadRequestResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class MediaDataSourceImpl @Inject constructor(
    private val client: HttpClient,
    @RawHttpClient private val rawClient: HttpClient,
) : MediaDataSource {
    override suspend fun createUploadRequest(
        request: CreateUploadRequestRequest,
    ): UploadRequestResponse = client.post("media-assets/upload-requests") {
        contentType(ContentType.Application.Json)
        setBody(request)
    }.body<ApiResponse<UploadRequestResponse>>().data

    override suspend fun uploadImageBytes(uploadUrl: String, contentType: String, bytes: ByteArray) {
        rawClient.put(uploadUrl) {
            contentType(ContentType.parse(contentType))
            setBody(bytes)
        }
    }

    override suspend fun confirmUpload(mediaId: Long): MediaAssetResponse =
        client.post("media-assets/$mediaId/confirm")
            .body<ApiResponse<MediaAssetResponse>>().data
}
