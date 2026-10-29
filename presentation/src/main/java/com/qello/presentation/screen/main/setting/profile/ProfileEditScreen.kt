package com.qello.presentation.screen.main.setting.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qello.presentation.R
import com.qello.presentation.common.rememberSinglePhotoPicker
import com.qello.presentation.designsystem.component.button.QelloBackButton
import com.qello.presentation.designsystem.component.button.QelloLargeButton
import com.qello.presentation.designsystem.component.item.QelloProfileImageField
import com.qello.presentation.designsystem.component.loading.QelloLoadingOverlay
import com.qello.presentation.designsystem.component.spacer.QelloSpacer
import com.qello.presentation.designsystem.component.text.QelloText
import com.qello.presentation.designsystem.component.text.QelloTextField
import com.qello.presentation.designsystem.theme.QelloColorPalette
import com.qello.presentation.designsystem.theme.QelloTheme
import com.qello.presentation.mapper.message

private val ProfileImageSize = 160.dp

@Composable
fun ProfileEditScreen(
    onBack: () -> Unit,
    showSnackbar: suspend (message: String) -> Unit,
    viewModel: ProfileEditViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val resources = LocalResources.current

    val photoPicker = rememberSinglePhotoPicker(
        onPhotoPicked = viewModel::onProfileImagePicked,
    )

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is ProfileEditSideEffect.ShowSnackbar -> showSnackbar(resources.getString(effect.message))
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(QelloTheme.gradient.backgroundStrong),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = QelloTheme.spacing.spacing18)
                    .padding(vertical = QelloTheme.spacing.spacing36),
            ) {
                QelloBackButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart))

                QelloText(
                    text = stringResource(R.string.profile_edit_title),
                    style = QelloTheme.typography.heading2,
                    color = QelloTheme.colors.label.strong,
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = QelloTheme.spacing.spacing20),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                QelloSpacer(QelloTheme.spacing.spacing8)

                // 새로 고른 사진이 있으면 미리보기, 없으면 현재 프로필 이미지
                QelloProfileImageField(
                    imageUri = uiState.displayImage,
                    onClick = photoPicker::launch,
                    size = ProfileImageSize,
                    contentDescription = stringResource(R.string.profile_edit_image_description),
                )

                QelloSpacer(QelloTheme.spacing.spacing12)

                QelloText(
                    text = stringResource(R.string.profile_edit_image_change),
                    style = QelloTheme.typography.caption2,
                    color = QelloTheme.colors.label.neutral,
                    modifier = Modifier
                        .background(QelloColorPalette.Navy20, RoundedCornerShape(QelloTheme.radius.radiusFull))
                        .border(
                            QelloTheme.borderWidth.border1,
                            QelloTheme.colors.line.normal,
                            RoundedCornerShape(QelloTheme.radius.radiusFull),
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            role = Role.Button,
                            onClick = photoPicker::launch,
                        )
                        .padding(
                            horizontal = QelloTheme.spacing.spacing14,
                            vertical = QelloTheme.spacing.spacing6,
                        ),
                )

                QelloSpacer(QelloTheme.spacing.spacing40)

                QelloTextField(
                    value = uiState.nicknameInput,
                    onValueChange = viewModel::onNicknameChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.profile_edit_nickname_label),
                    placeholder = stringResource(R.string.profile_edit_nickname_placeholder),
                    isError = uiState.nicknameError != null,
                    supportingText = uiState.nicknameError?.message(),
                )
            }

            QelloLargeButton(
                text = stringResource(R.string.profile_edit_save),
                // QelloLargeButton에 비활성 상태가 없어, 바꾼 내용이 없을 때는 어두운 버튼으로 보여준다 (클릭은 ViewModel에서 무시)
                colors = if (uiState.canSave) {
                    QelloTheme.buttonColors.lightButtonColors
                } else {
                    QelloTheme.buttonColors.darkButtonColors
                },
                modifier = Modifier
                    .padding(horizontal = QelloTheme.spacing.spacing20)
                    .padding(bottom = QelloTheme.spacing.spacing24),
                onClick = viewModel::onSaveClick,
            )
        }

        if (uiState.isLoading) {
            QelloLoadingOverlay()
        }
    }
}
