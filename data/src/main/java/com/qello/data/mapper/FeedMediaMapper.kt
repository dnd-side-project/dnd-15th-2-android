package com.qello.data.mapper

import com.qello.data.remote.model.common.FeedMediaResponse
import com.qello.domain.model.FeedMedia

internal fun FeedMediaResponse.toDomain(): FeedMedia = FeedMedia(mediaId = mediaId, url = url)
