package com.qello.domain.model

data class UserProfile(
    val userId: Long,
    val nickname: String,
    val profileImageUrl: String,
    val profileImageExpiresAt: String,
    val usesDefaultProfileImage: Boolean,
)
