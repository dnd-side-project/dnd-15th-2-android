package com.qello.presentation.media

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.VisualMediaType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

/**
 * 포토피커 실행 핸들
 * 추후 복수 선택 대비
 */
@Stable
class PhotoPickerLauncher internal constructor(
    private val onLaunch: () -> Unit,
) {
    fun launch() = onLaunch()
}


@Composable
fun rememberSinglePhotoPicker(
    mediaType: VisualMediaType = ActivityResultContracts.PickVisualMedia.ImageOnly,
    onPhotoPicked: (uri: String) -> Unit,
): PhotoPickerLauncher {
    val currentOnPhotoPicked by rememberUpdatedState(onPhotoPicked)

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia(),
    ) { uri ->
        uri?.let { currentOnPhotoPicked(it.toString()) }
    }

    return remember(launcher, mediaType) {
        PhotoPickerLauncher { launcher.launch(PickVisualMediaRequest(mediaType)) }
    }
}
