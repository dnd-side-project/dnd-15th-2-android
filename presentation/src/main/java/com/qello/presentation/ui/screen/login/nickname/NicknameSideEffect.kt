package com.qello.presentation.ui.screen.login.nickname

sealed interface NicknameSideEffect {
    data class NavigateToWelcome(val nickname: String) : NicknameSideEffect
}
