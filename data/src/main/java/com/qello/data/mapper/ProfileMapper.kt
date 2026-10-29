package com.qello.data.mapper

import com.qello.data.remote.model.profile.MyProfileResponse
import com.qello.domain.model.UserProfile

internal fun MyProfileResponse.toDomain(): UserProfile = UserProfile(
    userId = userId,
    nickname = nickname,
    profileImageUrl = profileImageUrl,
    profileImageExpiresAt = profileImageExpiresAt,
    usesDefaultProfileImage = usesDefaultProfileImage,
)
