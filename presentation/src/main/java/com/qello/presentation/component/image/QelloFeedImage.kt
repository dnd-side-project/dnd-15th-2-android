package com.qello.presentation.component.image

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import com.qello.domain.model.FeedMedia
import com.qello.presentation.ui.designsystem.theme.QelloTheme

private const val PLACEHOLDER_ASPECT_RATIO = 16f / 10f

// 조회 URL은 받을 때마다 바뀌므로 Coil 캐시 키는 URL이 아니라 mediaId로 고정한다.
@Composable
private fun rememberFeedImageRequest(photo: FeedMedia): ImageRequest {
    val context = LocalContext.current
    return ImageRequest.Builder(context)
        .data(photo.url)
        .memoryCacheKey(photo.mediaId.toString())
        .diskCacheKey(photo.mediaId.toString())
        .build()
}

/** 정해진 영역을 꽉 채워 자르는 이미지. 목록 카드처럼 크기가 이미 정해진 곳에서 쓴다. */
@Composable
fun QelloFeedCropImage(
    photo: FeedMedia,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = rememberFeedImageRequest(photo),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier.background(QelloTheme.colors.imagefield.default),
    )
}

/** 원본 비율 그대로 가로를 채우는 이미지. 불러오는 동안과 실패했을 때는 회색 박스를 보여준다. */
@Composable
fun QelloFeedImage(
    photo: FeedMedia,
    modifier: Modifier = Modifier,
) {
    SubcomposeAsyncImage(
        model = rememberFeedImageRequest(photo),
        contentDescription = null,
        contentScale = ContentScale.FillWidth,
        modifier = modifier.fillMaxWidth(),
        loading = { FeedImagePlaceholder() },
        error = { FeedImagePlaceholder() },
    )
}

@Composable
private fun FeedImagePlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(PLACEHOLDER_ASPECT_RATIO)
            .background(QelloTheme.colors.imagefield.default),
    )
}
