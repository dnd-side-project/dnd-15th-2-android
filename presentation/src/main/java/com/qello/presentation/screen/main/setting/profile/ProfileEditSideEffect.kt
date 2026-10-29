package com.qello.presentation.screen.main.setting.profile

sealed interface ProfileEditSideEffect {
    data class ShowSnackbar(val message: Int) : ProfileEditSideEffect
}
