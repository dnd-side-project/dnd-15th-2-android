package com.qello.presentation.ui.screen.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mapbox.geojson.Point
import com.qello.presentation.R
import com.qello.presentation.component.bottomsheet.QelloBottomSheet
import com.qello.presentation.component.button.QelloLargeButton
import com.qello.presentation.component.map.QelloMap
import com.qello.presentation.component.spacer.QelloSpacer
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.designsystem.theme.QelloTheme
import com.qello.presentation.ui.screen.login.component.AppIntroSheetContent
import com.qello.presentation.ui.screen.login.component.PermissionSheetContent
import com.qello.presentation.ui.screen.login.component.SettingsGuideDialog
import com.qello.presentation.ui.screen.login.permission.rememberLoginPermissionRequester

@Composable
fun LoginScreen(
    onNavigateToNickname: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentOnNavigateToNickname by rememberUpdatedState(onNavigateToNickname)

    val permissionRequester = rememberLoginPermissionRequester(
        onResult = viewModel::onPermissionRequestResult,
    )

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refreshPermissionState()
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is LoginSideEffect.RequestPermission -> permissionRequester.request(effect.permission)
                is LoginSideEffect.OpenAppSettings -> permissionRequester.openAppSettings()
                is LoginSideEffect.NavigateToNickname -> currentOnNavigateToNickname()
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        QelloMap(
            initialCenter = Point.fromLngLat(0.0, 20.0),
            initialZoom = 1.0,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = QelloTheme.spacing.spacing20)
                .padding(bottom = QelloTheme.spacing.spacing24),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            QelloText(
                text = stringResource(R.string.login_active_question_count),
                style = QelloTheme.typography.caption1,
                color = QelloTheme.colors.label.normal2,
            )

            QelloSpacer(QelloTheme.spacing.spacing12)

            QelloLargeButton(
                text = stringResource(R.string.login_button),
                onClick = { viewModel.onLoginClick() },
            )
        }
    }

    uiState.sheetStep?.let { step ->
        QelloBottomSheet(
            onDismissRequest = { viewModel.onSheetDismissed() },
        ) {
            when (step) {
                LoginSheetStep.APP_INTRO -> AppIntroSheetContent(
                    onNextClick = { viewModel.onAppIntroNextClick() },
                )

                LoginSheetStep.PERMISSION -> PermissionSheetContent(
                    isLocationGranted = uiState.isLocationGranted,
                    isNotificationGranted = uiState.isNotificationGranted,
                    onPermissionItemClick = viewModel::onPermissionItemClick,
                    onAllowClick = { viewModel.onAllowClick() },
                )
            }
        }
    }

    uiState.settingsGuidePermission?.let { permission ->
        SettingsGuideDialog(
            permission = permission,
            onConfirm = { viewModel.onSettingsGuideConfirm() },
            onDismiss = { viewModel.onSettingsGuideDismiss() },
        )
    }
}
