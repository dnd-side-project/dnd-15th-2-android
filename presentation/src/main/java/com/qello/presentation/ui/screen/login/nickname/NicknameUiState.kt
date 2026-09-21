package com.qello.presentation.ui.screen.login.nickname

import com.qello.domain.validation.NicknameError

data class NicknameUiState(
    val nickname: String = "",
    val profileImageUri: String? = null,
    val nicknameError: NicknameError? = null,
    val isSubmitting: Boolean = false,
)
