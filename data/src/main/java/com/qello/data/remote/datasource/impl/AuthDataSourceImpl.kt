package com.qello.data.remote.datasource.impl

import com.qello.data.remote.datasource.AuthDataSource
import com.qello.data.remote.model.auth.RegisterDeviceRequest
import com.qello.data.remote.model.common.ApiResponse
import com.qello.data.remote.model.auth.RegisterDeviceResponse
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
        request: RegisterDeviceRequest,
    ): RegisterDeviceResponse = client.post("auth/devices") {
        contentType(ContentType.Application.Json)
        setBody(request)
    }.body<ApiResponse<RegisterDeviceResponse>>().data
}
