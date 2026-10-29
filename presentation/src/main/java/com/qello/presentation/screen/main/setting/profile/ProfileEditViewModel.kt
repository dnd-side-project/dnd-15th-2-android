package com.qello.presentation.screen.main.setting.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qello.domain.model.UserProfile
import com.qello.domain.repository.MediaRepository
import com.qello.domain.repository.ProfileRepository
import com.qello.domain.result.AppError
import com.qello.domain.result.AppResult
import com.qello.domain.result.asResult
import com.qello.domain.validation.NicknameError
import com.qello.domain.validation.NicknameValidator
import com.qello.presentation.R
import com.qello.presentation.mapper.toMessageRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileEditViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val profileRepository: ProfileRepository,
    private val mediaRepository: MediaRepository,
) : ViewModel() {
    private val nicknameInput = savedStateHandle.getStateFlow<String?>(KEY_NICKNAME, null)

    private val pickedImageUri = MutableStateFlow<String?>(null)

    private val nicknameError = MutableStateFlow<NicknameError?>(null)

    private val isLoading = MutableStateFlow(false)

    private var uploadedMediaId: Long? = null

    val uiState: StateFlow<ProfileEditUiState> = combine(
        profileRepository.profile
            .map<UserProfile, UserProfile?> { it }
            .onStart { emit(null) },
        nicknameInput,
        pickedImageUri,
        nicknameError,
        isLoading,
    ) { profile, nicknameInput, pickedImageUri, nicknameError, isLoading ->
        ProfileEditUiState(
            isProfileLoaded = profile != null,
            currentNickname = profile?.nickname.orEmpty(),
            currentImageUrl = profile?.profileImageUrl,
            nicknameInput = nicknameInput ?: profile?.nickname.orEmpty(),
            pickedImageUri = pickedImageUri,
            nicknameError = nicknameError,
            isLoading = isLoading,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = ProfileEditUiState(),
    )

    private val _sideEffect = Channel<ProfileEditSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<ProfileEditSideEffect> = _sideEffect.receiveAsFlow()

    init {
        loadProfile()
    }

    fun onNicknameChanged(nickname: String) {
        savedStateHandle[KEY_NICKNAME] = nickname
        nicknameError.value = null
    }

    fun onProfileImagePicked(uri: String) {
        pickedImageUri.value = uri
        uploadedMediaId = null
    }

    fun onSaveClick() {
        val state = uiState.value
        if (!state.canSave) return

        val newNickname = state.nicknameInput.trim().takeIf { state.isNicknameChanged }
        newNickname?.let { nickname ->
            NicknameValidator.validate(nickname)?.let { error ->
                nicknameError.value = error
                return
            }
        }

        viewModelScope.launch {
            isLoading.value = true
            val isSaved = updateNicknameIfChanged(newNickname) && updateImageIfPicked(state.pickedImageUri)
            isLoading.value = false

            if (isSaved) {
                _sideEffect.send(ProfileEditSideEffect.ShowSnackbar(R.string.profile_edit_success))
            }
        }
    }

    private fun loadProfile() {
        suspend { profileRepository.refreshProfileIfStale() }
            .asFlow()
            .asResult()
            .onEach { result ->
                when (result) {
                    AppResult.Loading -> isLoading.value = true

                    is AppResult.Success -> isLoading.value = false

                    is AppResult.Error -> {
                        isLoading.value = false
                        _sideEffect.send(ProfileEditSideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private suspend fun updateNicknameIfChanged(nickname: String?): Boolean {
        if (nickname == null) return true

        return try {
            profileRepository.updateNickname(nickname)
            savedStateHandle[KEY_NICKNAME] = null
            true
        } catch (error: AppError) {
            handleNicknameError(error)
            false
        }
    }

    private suspend fun updateImageIfPicked(imageUri: String?): Boolean {
        if (imageUri == null) return true

        return try {
            val mediaId = uploadedMediaId ?: run {
                val mediaAsset = mediaRepository.uploadImage(imageUri)
                if (!mediaAsset.isReady) {
                    _sideEffect.send(ProfileEditSideEffect.ShowSnackbar(R.string.profile_edit_image_rejected))
                    return false
                }
                mediaAsset.mediaId.also { uploadedMediaId = it }
            }

            profileRepository.resetProfileImage()
            profileRepository.updateProfileImage(mediaId)

            pickedImageUri.value = null
            uploadedMediaId = null
            true
        } catch (error: AppError) {
            _sideEffect.send(ProfileEditSideEffect.ShowSnackbar(error.toMessageRes()))
            false
        }
    }

    private suspend fun handleNicknameError(error: AppError) {
        if (error is AppError.Server) {
            when (error.status) {
                400 -> {
                    nicknameError.value = NicknameError.INAPPROPRIATE
                    return
                }
                409 -> {
                    _sideEffect.send(ProfileEditSideEffect.ShowSnackbar(R.string.profile_edit_nickname_unavailable))
                    return
                }
            }
        }
        _sideEffect.send(ProfileEditSideEffect.ShowSnackbar(error.toMessageRes()))
    }

    private companion object {
        const val KEY_NICKNAME = "nickname"
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
