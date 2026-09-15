package com.qello.presentation.ui.screen.login.permission

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.core.app.ActivityCompat

private val LOCATION_PERMISSIONS = arrayOf(
    Manifest.permission.ACCESS_FINE_LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION,
)

@Stable
class LoginPermissionRequester internal constructor(
    private val activity: Activity,
    private val launchLocation: () -> Unit,
    private val launchNotification: () -> Unit,
) {
    fun request(permission: LoginPermission) {
        when (permission) {
            LoginPermission.LOCATION -> launchLocation()
            LoginPermission.NOTIFICATION -> launchNotification()
        }
    }

    fun openAppSettings() {
        activity.startActivity(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.packageName, null),
            ),
        )
    }
}

/**
 * 권한 요청 실행과 "영구 거부" 판정을 담당하는 컴포즈함수
 *
 * 요청 결과가 denied이고 shouldShowRequestPermissionRationale()가 false이면
 * OS가 더 이상 다이얼로그를 띄우지 않는 상태다. 이때는 launch()를 반복해도
 * 즉시 denied가 돌아오므로, 앱 설정 화면으로 유도하는 것이 유일한 경로다.
 */
@Composable
fun rememberLoginPermissionRequester(
    onResult: (permission: LoginPermission, isPermanentlyDenied: Boolean) -> Unit,
): LoginPermissionRequester {
    val activity = requireNotNull(LocalActivity.current) {
        "LoginScreen must be hosted in an Activity"
    }
    val currentOnResult by rememberUpdatedState(onResult)

    val locationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { results ->
        val granted = results.values.any { it }
        val permanentlyDenied = !granted && LOCATION_PERMISSIONS.none {
            ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
        }
        currentOnResult(LoginPermission.LOCATION, permanentlyDenied)
    }

    val notificationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        val permanentlyDenied = !granted &&
            !ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.POST_NOTIFICATIONS,
            )
        currentOnResult(LoginPermission.NOTIFICATION, permanentlyDenied)
    }

    return remember(activity) {
        LoginPermissionRequester(
            activity = activity,
            launchLocation = { locationLauncher.launch(LOCATION_PERMISSIONS) },
            launchNotification = {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    currentOnResult(LoginPermission.NOTIFICATION, false)
                }
            },
        )
    }
}
