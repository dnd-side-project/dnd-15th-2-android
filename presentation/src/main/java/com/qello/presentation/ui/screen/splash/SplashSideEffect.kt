package com.qello.presentation.ui.screen.splash

sealed interface SplashSideEffect {
    data object NavigateToLogin : SplashSideEffect
    data object NavigateToMain : SplashSideEffect
}
