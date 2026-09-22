package com.qello.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    val installationId: String,
    val deviceSecret: String,
)
