package com.qello.data.remote.datasource

import com.qello.data.remote.response.MediaAssetResponse
import com.qello.data.remote.response.UploadRequestResponse

interface MediaDataSource {
    /** 이미지를 올릴 임시 주소(uploadUrl)를 발급받는다. */
    suspend fun createUploadRequest(byteSize: Long, checksum: String, contentType: String): UploadRequestResponse

    /** 발급받은 uploadUrl에 이미지 바이트를 직접 PUT으로 올린다(우리 서버가 아닌 스토리지로 직행). */
    suspend fun uploadImageBytes(uploadUrl: String, contentType: String, bytes: ByteArray)

    /** 실제로 잘 올라갔는지 서버에 확인시켜, 첨부 가능한 상태로 바꾼다. */
    suspend fun confirmUpload(mediaId: Long): MediaAssetResponse
}
