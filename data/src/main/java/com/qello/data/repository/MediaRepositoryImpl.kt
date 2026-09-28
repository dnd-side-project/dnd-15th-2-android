package com.qello.data.repository

import com.qello.data.media.ImageFileReader
import com.qello.data.remote.datasource.MediaDataSource
import com.qello.domain.model.MediaAsset
import com.qello.domain.repository.MediaRepository
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val mediaDataSource: MediaDataSource,
    private val imageFileReader: ImageFileReader,
) : MediaRepository {
    override suspend fun uploadImage(imageUri: String): MediaAsset {
        val image = imageFileReader.read(imageUri)

        val uploadRequest = mediaDataSource.createUploadRequest(
            byteSize = image.bytes.size.toLong(),
            checksum = image.checksum,
            contentType = image.contentType,
        )

        mediaDataSource.uploadImageBytes(
            uploadUrl = uploadRequest.uploadUrl,
            contentType = image.contentType,
            bytes = image.bytes,
        )

        val confirmed = mediaDataSource.confirmUpload(uploadRequest.mediaId)

        return MediaAsset(mediaId = confirmed.mediaId)
    }
}
