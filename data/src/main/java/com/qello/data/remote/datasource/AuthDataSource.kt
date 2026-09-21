package com.qello.data.remote.datasource

import com.qello.data.remote.response.RegisterDeviceResponse

interface AuthDataSource {
    suspend fun registerDevice(installationId: String, nickname: String): RegisterDeviceResponse
}
