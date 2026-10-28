package com.qello.presentation.screen.main.setting.profile

import com.qello.domain.validation.NicknameError

data class ProfileEditUiState(
    val isProfileLoaded: Boolean = false,
    val currentNickname: String = "",
    val currentImageUrl: String? = null,
    val nicknameInput: String = "",
    val pickedImageUri: String? = null,
    val nicknameError: NicknameError? = null,
    val isLoading: Boolean = false,
) {
    val displayImage: String?
        get() = pickedImageUri ?: currentImageUrl

    val isNicknameChanged: Boolean
        get() = nicknameInput.trim() != currentNickname

    val canSave: Boolean
        get() = isProfileLoaded && !isLoading && (isNicknameChanged || pickedImageUri != null)
}
