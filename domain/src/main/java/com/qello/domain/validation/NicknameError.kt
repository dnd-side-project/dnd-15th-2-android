package com.qello.domain.validation

enum class NicknameError {
    BLANK,
    OUT_OF_LENGTH,
    DUPLICATED,     // 서버 409
    INAPPROPRIATE, // 서버 400
}
