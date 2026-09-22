package com.qello.presentation.ui.screen.login

import com.qello.presentation.ui.screen.login.permission.LoginPermission

sealed interface LoginSideEffect {
    data class RequestPermission(val permission: LoginPermission) : LoginSideEffect
    data object OpenAppSettings : LoginSideEffect
    data object NavigateToNickname : LoginSideEffect
}
