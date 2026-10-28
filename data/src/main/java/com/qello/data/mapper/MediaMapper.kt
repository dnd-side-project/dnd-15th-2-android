package com.qello.data.mapper

import com.qello.data.remote.model.media.MediaAssetResponse
import com.qello.domain.model.MediaAsset

private const val MEDIA_STATUS_READY = "READY"

internal fun MediaAssetResponse.toDomain(): MediaAsset = MediaAsset(
    mediaId = mediaId,
    isReady = status == MEDIA_STATUS_READY,
)
