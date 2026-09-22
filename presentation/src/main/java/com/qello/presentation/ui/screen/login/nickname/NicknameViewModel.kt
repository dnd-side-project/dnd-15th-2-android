package com.qello.presentation.ui.screen.login.nickname

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qello.domain.repository.UserAccountRepository
import com.qello.domain.result.AppError
import com.qello.domain.result.AppResult
import com.qello.domain.result.asResult
import com.qello.domain.validation.NicknameError
import com.qello.domain.validation.NicknameValidator
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
import javax.inject.Inject

@HiltViewModel
class NicknameViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userAccountRepository: UserAccountRepository,
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

        suspend { userAccountRepository.createAccount(trimmedNickname) }
            .asFlow()
            .asResult()
            .onEach { result ->
                when (result) {
                    AppResult.Loading -> isSubmitting.value = true

                    is AppResult.Success -> {
                        isSubmitting.value = false
                        _sideEffect.send(NicknameSideEffect.NavigateToWelcome(trimmedNickname))
                    }

                    is AppResult.Error -> {
                        isSubmitting.value = false
                        handleSignUpError(result.error)
                    }
                }
            }
            .launchIn(viewModelScope)
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
