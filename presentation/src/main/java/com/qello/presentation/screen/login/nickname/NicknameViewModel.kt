package com.qello.presentation.screen.login.nickname

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qello.domain.repository.MediaRepository
import com.qello.domain.repository.ProfileRepository
import com.qello.domain.repository.UserAccountRepository
import com.qello.domain.result.AppError
import com.qello.domain.validation.NicknameError
import com.qello.domain.validation.NicknameValidator
import com.qello.presentation.mapper.toMessageRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NicknameViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userAccountRepository: UserAccountRepository,
    private val mediaRepository: MediaRepository,
    private val profileRepository: ProfileRepository,
) : ViewModel(){
    private val nickname = savedStateHandle.getStateFlow(KEY_NICKNAME, "")

    private val profileImageUri = MutableStateFlow<String?>(null)

    private val nicknameError = MutableStateFlow<NicknameError?>(null)

    private val isSubmitting = MutableStateFlow(false)

    val uiState: StateFlow<NicknameUiState> = combine(
        nickname,
        profileImageUri,
        nicknameError,
        isSubmitting
    ) { nickname, profileImageUri, nicknameError, isSubmitting ->
        NicknameUiState(
            nickname = nickname,
            profileImageUri = profileImageUri,
            nicknameError = nicknameError,
            isSubmitting = isSubmitting,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = NicknameUiState(),
    )

    private val _sideEffect = Channel<NicknameSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<NicknameSideEffect> = _sideEffect.receiveAsFlow()

    fun onNicknameChanged(nickname: String) {
        savedStateHandle[KEY_NICKNAME] = nickname
        nicknameError.value = null
    }

    fun onProfileImagePicked(uri: String) {
        profileImageUri.value = uri
    }

    fun onSignUpClick() {
        val trimmedNickname = nickname.value.trim()

        NicknameValidator.validate(trimmedNickname)?.let { error ->
            nicknameError.value = error
            return
        }

        viewModelScope.launch {
            isSubmitting.value = true

            val isRegistered = try {
                userAccountRepository.createAccount(trimmedNickname)
                true
            } catch (error: AppError) {
                handleSignUpError(error)
                false
            }

            if (isRegistered) {
                profileImageUri.value?.let { imageUri -> registerProfileImage(imageUri) }
                isSubmitting.value = false
                _sideEffect.send(NicknameSideEffect.NavigateToWelcome(trimmedNickname))
            } else {
                isSubmitting.value = false
            }
        }
    }

    private suspend fun registerProfileImage(imageUri: String) {
        try {
            val mediaAsset = mediaRepository.uploadImage(imageUri)

            if (!mediaAsset.isReady) {
                Timber.w("가입 후 프로필 사진이 거절됨(REJECTED), mediaId=${mediaAsset.mediaId}")
                return
            }

            profileRepository.updateProfileImage(mediaAsset.mediaId)
        } catch (error: AppError) {
            Timber.w(error, "가입 후 프로필 사진 등록 실패")
        }
    }

    private suspend fun handleSignUpError(error: AppError) {
        val fieldError = error.toNicknameError()
        if (fieldError != null) {
            nicknameError.value = fieldError
        } else {
            _sideEffect.send(NicknameSideEffect.ShowSnackbar(error.toMessageRes()))
        }
    }

    private fun AppError.toNicknameError(): NicknameError? {
        if (this !is AppError.Server) return null
        return when (status) {
            409 -> NicknameError.DUPLICATED
            400 -> NicknameError.INAPPROPRIATE
            else -> null
        }
    }

    private companion object {
        const val KEY_NICKNAME = "nickname"
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
