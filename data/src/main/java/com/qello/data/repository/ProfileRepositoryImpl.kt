package com.qello.data.repository

import com.qello.data.local.datastore.UserPreferencesDataSource
import com.qello.data.mapper.toDomain
import com.qello.data.remote.datasource.ProfileDataSource
import com.qello.data.remote.model.profile.UpdateNicknameRequest
import com.qello.data.remote.model.profile.UpdateProfileImageRequest
import com.qello.domain.model.UserProfile
import com.qello.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import java.time.Instant
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileDataSource: ProfileDataSource,
    private val userPreferencesDataSource: UserPreferencesDataSource,
) : ProfileRepository {
    private val cachedProfile = MutableStateFlow<UserProfile?>(null)

    override val profile: Flow<UserProfile> = cachedProfile.filterNotNull()

    override suspend fun refreshProfileIfStale() {
        val current = cachedProfile.value
        if (current != null && !current.isImageExpired()) return

        cachedProfile.value = profileDataSource.getMyProfile().toDomain()
    }

    override suspend fun updateNickname(nickname: String) {
        val updatedNickname = profileDataSource.updateNickname(UpdateNicknameRequest(nickname)).nickname

        cachedProfile.update { it?.copy(nickname = updatedNickname) }
        userPreferencesDataSource.updateNickname(updatedNickname)
    }

    override suspend fun updateProfileImage(mediaId: Long) {
        cachedProfile.value = profileDataSource.updateProfileImage(UpdateProfileImageRequest(mediaId)).toDomain()
    }

    override suspend fun resetProfileImage() {
        cachedProfile.value = profileDataSource.deleteProfileImage().toDomain()
    }

    private fun UserProfile.isImageExpired(): Boolean =
        Instant.parse(profileImageExpiresAt).isBefore(Instant.now().plusSeconds(EXPIRY_MARGIN_SECONDS))

    private companion object {
        // 만료 직전에 받은 주소로 이미지를 그리다 실패하지 않도록 여유를 둔다
        const val EXPIRY_MARGIN_SECONDS = 60L
    }
}
