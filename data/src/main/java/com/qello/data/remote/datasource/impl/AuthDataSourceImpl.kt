package com.qello.data.remote.datasource.impl

import com.qello.data.remote.datasource.AuthDataSource
import com.qello.data.remote.request.RegisterDeviceRequest
import com.qello.data.remote.response.ApiResponse
import com.qello.data.remote.response.RegisterDeviceResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

class AuthDataSourceImpl @Inject constructor(
    private val client: HttpClient,
) : AuthDataSource {
    override suspend fun registerDevice(
        installationId: String,
        nickname: String,
    ): RegisterDeviceResponse = client.post("auth/devices") {
        contentType(ContentType.Application.Json)
        setBody(
            RegisterDeviceRequest(
                installationId = installationId,
                nickname = nickname,
                platform = "ANDROID",
                timezone = "Asia/Seoul",
                countryCode = "KR",
                coarseRegionCode = "KR-11",
                locale = "ko_KR",
            ),
        )
    }.body<ApiResponse<RegisterDeviceResponse>>().data
}
