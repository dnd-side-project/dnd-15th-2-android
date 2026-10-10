package com.qello.data.remote.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDeviceResponse(
    val userId: Long,
    val accessToken: String,
    val expiresIn: Long,
    val deviceSecret: String,
)
