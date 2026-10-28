package com.qello.domain.repository

import com.qello.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    val profile: Flow<UserProfile>

    suspend fun refreshProfileIfStale()

    suspend fun updateNickname(nickname: String)

    suspend fun updateProfileImage(mediaId: Long)

    suspend fun resetProfileImage()
}
