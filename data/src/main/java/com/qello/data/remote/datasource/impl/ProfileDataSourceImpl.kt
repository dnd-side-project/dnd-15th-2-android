package com.qello.data.remote.datasource.impl

import com.qello.data.remote.datasource.ProfileDataSource
import com.qello.data.remote.model.common.ApiResponse
import com.qello.data.remote.model.profile.MyProfileResponse
import com.qello.data.remote.model.profile.UpdateNicknameRequest
import com.qello.data.remote.model.profile.UpdateNicknameResponse
import com.qello.data.remote.model.profile.UpdateProfileImageRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class ProfileDataSourceImpl @Inject constructor(
    private val client: HttpClient,
) : ProfileDataSource {
    override suspend fun getMyProfile(): MyProfileResponse =
        client.get("me/profile").body<ApiResponse<MyProfileResponse>>().data

    override suspend fun updateNickname(
        request: UpdateNicknameRequest,
    ): UpdateNicknameResponse = client.patch("users/me/nickname") {
        contentType(ContentType.Application.Json)
        setBody(request)
    }.body<ApiResponse<UpdateNicknameResponse>>().data

    override suspend fun updateProfileImage(
        request: UpdateProfileImageRequest,
    ): MyProfileResponse = client.put("me/profile/image") {
        contentType(ContentType.Application.Json)
        setBody(request)
    }.body<ApiResponse<MyProfileResponse>>().data

    override suspend fun deleteProfileImage(): MyProfileResponse =
        client.delete("me/profile/image").body<ApiResponse<MyProfileResponse>>().data
}
