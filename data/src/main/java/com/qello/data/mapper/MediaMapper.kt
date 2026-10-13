package com.qello.data.mapper

import com.qello.data.remote.model.media.MediaAssetResponse
import com.qello.domain.model.MediaAsset

internal fun MediaAssetResponse.toDomain(): MediaAsset = MediaAsset(mediaId = mediaId)
