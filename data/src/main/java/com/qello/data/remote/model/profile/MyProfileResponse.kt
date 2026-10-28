package com.qello.data.remote.model.profile

import kotlinx.serialization.Serializable

@Serializable
data class MyProfileResponse(
    val userId: Long,
    val nickname: String,
    val profileImageUrl: String,
    val profileImageExpiresAt: String,
    val usesDefaultProfileImage: Boolean,
)
