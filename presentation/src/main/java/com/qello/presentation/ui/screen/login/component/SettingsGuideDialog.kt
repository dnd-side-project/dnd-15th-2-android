package com.qello.presentation.ui.screen.login.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.qello.presentation.R
import com.qello.presentation.component.dialog.QelloDialog
import com.qello.presentation.ui.screen.login.permission.LoginPermission

/**
 * 권한이 영구 거부되어 런타임 요청이 더 이상 통하지 않을 때, 앱 설정 화면으로 유도한다.
 */
@Composable
fun SettingsGuideDialog(
    permission: LoginPermission,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val permissionName = stringResource(
        when (permission) {
            LoginPermission.LOCATION -> R.string.login_permission_location_title
            LoginPermission.NOTIFICATION -> R.string.login_permission_notification_title
        },
    )

    QelloDialog(
        title = stringResource(R.string.login_permission_settings_title),
        description = stringResource(R.string.login_permission_settings_message, permissionName),
        dismissText = stringResource(R.string.login_permission_settings_dismiss),
        onDismissClick = onDismiss,
        confirmText = stringResource(R.string.login_permission_settings_confirm),
        onConfirmClick = onConfirm,
    )
}
