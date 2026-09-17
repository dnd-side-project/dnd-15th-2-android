package com.qello.presentation.ui.screen.login.nickname

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qello.domain.repository.UserAccountRepository
import com.qello.domain.validation.NicknameError
import com.qello.domain.validation.NicknameValidator
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
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class NicknameViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val userAccountRepository: UserAccountRepository,
) : ViewModel(){
    private val nickname = savedStateHandle.getStateFlow(KEY_NICKNAME, "")

    private val profileImageUri = MutableStateFlow<String?>(null)

    private val nicknameError = MutableStateFlow<NicknameError?>(null)

    val uiState: StateFlow<NicknameUiState> = combine(
        nickname,
        profileImageUri,
        nicknameError,
    ) { nickname, profileImageUri, nicknameError ->
        NicknameUiState(
            nickname = nickname,
            profileImageUri = profileImageUri,
            nicknameError = nicknameError,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = NicknameUiState(),
    )

    private val _sideEffect = Channel<NicknameSideEffect>()
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
            try {
                userAccountRepository.createAccount(trimmedNickname)
                _sideEffect.send(NicknameSideEffect.NavigateToWelcome(trimmedNickname))
            } catch (ioException: IOException) {
                Timber.e(ioException, "Failed to create account")
            }
        }
    }

    private companion object {
        const val KEY_NICKNAME = "nickname"
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
