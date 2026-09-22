package com.qello.presentation.ui.screen.login

import com.qello.presentation.ui.screen.login.permission.LoginPermission

enum class LoginSheetStep {
    APP_INTRO,
    PERMISSION,
}

data class LoginUiState(
    val isLocationGranted: Boolean = false,
    val isNotificationGranted: Boolean = false,
    val sheetStep: LoginSheetStep? = null,
    val settingsGuidePermission: LoginPermission? = null,
) {
    fun isGranted(permission: LoginPermission): Boolean = when (permission) {
        LoginPermission.LOCATION -> isLocationGranted
        LoginPermission.NOTIFICATION -> isNotificationGranted
    }
}
