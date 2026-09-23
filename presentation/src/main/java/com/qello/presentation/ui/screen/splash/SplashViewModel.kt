package com.qello.presentation.ui.screen.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qello.domain.repository.UserAccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userAccountRepository: UserAccountRepository,
) : ViewModel() {

    private val _sideEffect = Channel<SplashSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<SplashSideEffect> = _sideEffect.receiveAsFlow()

    init {
        checkRegistration()
    }

    private fun checkRegistration() {
        viewModelScope.launch {
            if (userAccountRepository.isRegistered()) {
                _sideEffect.send(SplashSideEffect.NavigateToMain)
            } else {
                userAccountRepository.clearAccount()
                _sideEffect.send(SplashSideEffect.NavigateToLogin)
            }
        }
    }
}
