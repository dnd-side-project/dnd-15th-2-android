package com.qello.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDeviceResponse(
    val userId: Long,
    val accessToken: String,
    val expiresIn: Long,
    val deviceSecret: String,
)
