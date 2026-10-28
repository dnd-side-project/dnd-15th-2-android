package com.qello.presentation.screen.main.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qello.domain.repository.ProfileRepository
import com.qello.domain.result.AppResult
import com.qello.domain.result.asResult
import com.qello.presentation.mapper.toMessageRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
) : ViewModel() {
    val uiState: StateFlow<SettingUiState> = profileRepository.profile
        .map { profile ->
            SettingUiState(
                nickname = profile.nickname,
                profileImageUrl = profile.profileImageUrl,
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = SettingUiState(),
        )

    private val _sideEffect = Channel<SettingSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<SettingSideEffect> = _sideEffect.receiveAsFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        suspend { profileRepository.refreshProfileIfStale() }
            .asFlow()
            .asResult()
            .onEach { result ->
                if (result is AppResult.Error) {
                    _sideEffect.send(SettingSideEffect.ShowSnackbar(result.error.toMessageRes()))
                }
            }
            .launchIn(viewModelScope)
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
