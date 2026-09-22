package com.qello.presentation.ui.screen.login

import androidx.lifecycle.ViewModel
import com.qello.domain.permission.PermissionChecker
import com.qello.presentation.ui.screen.login.permission.LoginPermission
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val permissionChecker: PermissionChecker,
) : ViewModel() {
    private val _uiState = MutableStateFlow(createInitialState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<LoginSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<LoginSideEffect> = _sideEffect.receiveAsFlow()

    /**
     * 진입 시 최초 1회만 시트 단계를 결정한다.
     * - 둘 다 없음 → 앱 소개
     * - 하나만 없음 → 권한 시트
     * - 둘 다 있음 → 시트 없음
     */
    private fun createInitialState(): LoginUiState {
        val location = permissionChecker.isLocationPermissionGranted()
        val notification = permissionChecker.isNotificationPermissionGranted()
        return LoginUiState(
            isLocationGranted = location,
            isNotificationGranted = notification,
            sheetStep = when {
                location && notification -> null
                !location && !notification -> LoginSheetStep.APP_INTRO
                else -> LoginSheetStep.PERMISSION
            },
        )
    }

    /**
     * OS가 권한의 단일 진실 공급원이므로 포그라운드 복귀(ON_RESUME)마다 다시 읽는다.
     */
    fun refreshPermissionState() {
        _uiState.update { it.copy(
            isLocationGranted = permissionChecker.isLocationPermissionGranted(),
            isNotificationGranted = permissionChecker.isNotificationPermissionGranted(),
        ) }
    }

    fun onAppIntroNextClick() {
        _uiState.update { it.copy(sheetStep = LoginSheetStep.PERMISSION) }
    }

    fun onPermissionItemClick(permission: LoginPermission) {
        if (_uiState.value.isGranted(permission)) return
        _sideEffect.trySend(LoginSideEffect.RequestPermission(permission))
    }

    /** 필수 권한(위치)이 없으면 위치만 요청한다. */
    fun onAllowClick() {
        if (_uiState.value.isLocationGranted) {
            _uiState.update { it.copy(sheetStep = null) }
        } else {
            _sideEffect.trySend(LoginSideEffect.RequestPermission(LoginPermission.LOCATION))
        }
    }

    fun onSheetDismissed() {
        _uiState.update { it.copy(sheetStep = null) }
    }

    fun onPermissionRequestResult(permission: LoginPermission, isPermanentlyDenied: Boolean) {
        _uiState.update { it.copy(
            isLocationGranted = permissionChecker.isLocationPermissionGranted(),
            isNotificationGranted = permissionChecker.isNotificationPermissionGranted(),
            settingsGuidePermission = if (isPermanentlyDenied) permission else it.settingsGuidePermission,
        ) }
    }

    fun onSettingsGuideConfirm() {
        _uiState.update { it.copy(settingsGuidePermission = null) }
        _sideEffect.trySend(LoginSideEffect.OpenAppSettings)
    }

    fun onSettingsGuideDismiss() {
        _uiState.update { it.copy(settingsGuidePermission = null) }
    }

    /** 닉네임 화면 이동 전 필수 권한 재검사. 없으면 권한 시트를 다시 띄운다. */
    fun onLoginClick() {
        if (_uiState.value.isLocationGranted) {
            _sideEffect.trySend(LoginSideEffect.NavigateToNickname)
        } else {
            _uiState.update { it.copy(sheetStep = LoginSheetStep.PERMISSION) }
        }
    }
}
