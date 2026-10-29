package com.qello.presentation.screen.main.setting

sealed interface SettingSideEffect {
    data class ShowSnackbar(val message: Int) : SettingSideEffect
}
