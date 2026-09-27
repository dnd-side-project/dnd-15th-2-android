package com.qello.presentation.ui.screen.main.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qello.domain.repository.MediaRepository
import com.qello.domain.result.AppResult
import com.qello.domain.result.asResult
import com.qello.presentation.R
import com.qello.presentation.common.toMessageRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class QuestionComposeViewModel @Inject constructor(
    private val mediaRepository: MediaRepository,
) : ViewModel() {
    private val photoUri = MutableStateFlow<String?>(null)
    private val content = MutableStateFlow("")
    private val isUploadingPhoto = MutableStateFlow(false)
    private val uploadedMediaId = MutableStateFlow<Long?>(null)

    val uiState: StateFlow<QuestionComposeUiState> = combine(
        photoUri,
        content,
        isUploadingPhoto,
        uploadedMediaId,
    ) { photoUri, content, isUploadingPhoto, uploadedMediaId ->
        QuestionComposeUiState(
            photoUri = photoUri,
            content = content,
            isUploadingPhoto = isUploadingPhoto,
            uploadedMediaId = uploadedMediaId,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = QuestionComposeUiState(),
    )

    private val _sideEffect = Channel<QuestionComposeSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<QuestionComposeSideEffect> = _sideEffect.receiveAsFlow()

    fun onContentChanged(text: String) {
        content.value = text
    }

    fun onPhotoPicked(uri: String) {
        photoUri.value = uri
        uploadedMediaId.value = null

        suspend { mediaRepository.uploadImage(uri) }
            .asFlow()
            .asResult()
            .onEach { result ->
                when (result) {
                    AppResult.Loading -> isUploadingPhoto.value = true

                    is AppResult.Success -> {
                        isUploadingPhoto.value = false
                        uploadedMediaId.value = result.data.mediaId
                        Timber.d("이미지 업로드 완료, mediaId=${result.data.mediaId}")
                        _sideEffect.send(QuestionComposeSideEffect.ShowSnackbar(R.string.question_compose_photo_upload_success))
                    }

                    is AppResult.Error -> {
                        isUploadingPhoto.value = false
                        _sideEffect.send(QuestionComposeSideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
