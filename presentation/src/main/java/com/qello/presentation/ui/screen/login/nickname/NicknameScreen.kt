package com.qello.presentation.ui.screen.login.nickname

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import com.qello.domain.validation.NicknameError
import com.qello.domain.validation.NicknameValidator
import com.qello.presentation.R
import com.qello.presentation.component.button.QelloBackButton
import com.qello.presentation.component.button.QelloLargeButton
import com.qello.presentation.component.item.QelloProfileImageField
import com.qello.presentation.component.loading.QelloLoadingOverlay
import com.qello.presentation.component.spacer.QelloSpacer
import com.qello.presentation.component.text.QelloTextField
import com.qello.presentation.media.rememberSinglePhotoPicker
import com.qello.presentation.ui.designsystem.theme.QelloTheme

@Composable
fun NicknameScreen(
    onBackClick: () -> Unit,
    onNavigateToWelcome: (nickname: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NicknameViewModel = hiltViewModel(),
    showSnackbar: suspend (message: String) -> Unit,

) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentOnNavigateToWelcome by rememberUpdatedState(onNavigateToWelcome)

    val resources = LocalResources.current

    val photoPicker = rememberSinglePhotoPicker(
        onPhotoPicked = viewModel::onProfileImagePicked,
    )

    LaunchedEffect(viewModel) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is NicknameSideEffect.NavigateToWelcome -> currentOnNavigateToWelcome(effect.nickname)
                is NicknameSideEffect.ShowSnackbar -> showSnackbar(resources.getString(effect.message))
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(QelloTheme.colors.background.normalDefault)
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = QelloTheme.spacing.spacing24)
                .padding(horizontal = QelloTheme.spacing.spacing20)
        ) {
            QelloBackButton(onClick = dropUnlessResumed { onBackClick() })

            QelloProfileImageField(
                imageUri = uiState.profileImageUri,
                onClick = photoPicker::launch,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                contentDescription = stringResource(R.string.nickname_profile_image_description),
            )

            QelloSpacer(QelloTheme.spacing.spacing48)

            QelloTextField(
                value = uiState.nickname,
                onValueChange = viewModel::onNicknameChanged,
                label = stringResource(R.string.nickname_label),
                placeholder = stringResource(R.string.nickname_placeholder),
                isError = uiState.nicknameError != null,
                supportingText = uiState.nicknameError?.message(),
            )
        }

        QelloLargeButton(
            text = stringResource(R.string.nickname_sign_up),
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = QelloTheme.spacing.spacing20)
                .padding(bottom = QelloTheme.spacing.spacing24),
            onClick = viewModel::onSignUpClick,
        )
    }

    if (uiState.isSubmitting) {
        QelloLoadingOverlay()
    }
}

@Composable
private fun NicknameError.message(): String = when (this) {
    NicknameError.BLANK -> stringResource(R.string.nickname_error_blank)
    NicknameError.OUT_OF_LENGTH -> stringResource(
        R.string.nickname_error_out_of_length,
        NicknameValidator.MIN_LENGTH,
        NicknameValidator.MAX_LENGTH,
    )
    NicknameError.DUPLICATED -> stringResource(R.string.nickname_error_duplicated)
    NicknameError.INAPPROPRIATE -> stringResource(R.string.nickname_error_inappropriate)
}
