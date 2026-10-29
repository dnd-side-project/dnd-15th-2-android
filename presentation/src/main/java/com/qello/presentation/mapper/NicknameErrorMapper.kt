package com.qello.presentation.mapper

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.qello.domain.validation.NicknameError
import com.qello.domain.validation.NicknameValidator
import com.qello.presentation.R

@Composable
fun NicknameError.message(): String = when (this) {
    NicknameError.BLANK -> stringResource(R.string.nickname_error_blank)
    NicknameError.OUT_OF_LENGTH -> stringResource(
        R.string.nickname_error_out_of_length,
        NicknameValidator.MIN_LENGTH,
        NicknameValidator.MAX_LENGTH,
    )
    NicknameError.DUPLICATED -> stringResource(R.string.nickname_error_duplicated)
    NicknameError.INAPPROPRIATE -> stringResource(R.string.nickname_error_inappropriate)
}
