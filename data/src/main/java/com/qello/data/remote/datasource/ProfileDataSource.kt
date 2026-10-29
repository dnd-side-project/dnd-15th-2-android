package com.qello.data.remote.datasource

import com.qello.data.remote.model.profile.MyProfileResponse
import com.qello.data.remote.model.profile.UpdateNicknameRequest
import com.qello.data.remote.model.profile.UpdateNicknameResponse
import com.qello.data.remote.model.profile.UpdateProfileImageRequest

interface ProfileDataSource {
    suspend fun getMyProfile(): MyProfileResponse

    suspend fun updateNickname(request: UpdateNicknameRequest): UpdateNicknameResponse

    suspend fun updateProfileImage(request: UpdateProfileImageRequest): MyProfileResponse

    suspend fun deleteProfileImage(): MyProfileResponse
}
