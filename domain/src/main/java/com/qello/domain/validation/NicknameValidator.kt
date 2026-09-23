package com.qello.domain.validation

object NicknameValidator {
    const val MIN_LENGTH = 2
    const val MAX_LENGTH = 8

    fun validate(nickname: String): NicknameError? {
        val trimmed = nickname.trim()
        return when {
            trimmed.isEmpty() -> NicknameError.BLANK
            trimmed.length !in MIN_LENGTH..MAX_LENGTH -> NicknameError.OUT_OF_LENGTH
            else -> null
        }
    }
}
