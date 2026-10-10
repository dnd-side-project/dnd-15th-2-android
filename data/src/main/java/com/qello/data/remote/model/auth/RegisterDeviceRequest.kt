package com.qello.data.remote.model.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterDeviceRequest(
    val installationId: String,
    val nickname: String,
    val platform: String,
    val timezone: String,
    val countryCode: String,
    val coarseRegionCode: String,
    val locale: String,
)
