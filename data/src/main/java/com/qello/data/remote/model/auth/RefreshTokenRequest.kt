package com.qello.data.remote.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenRequest(
    val installationId: String,
    val deviceSecret: String,
)
