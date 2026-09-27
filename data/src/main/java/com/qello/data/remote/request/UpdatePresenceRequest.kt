package com.qello.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePresenceRequest(
    val latitude: Double,
    val longitude: Double,
    val accuracyMeters: Double,
    val receiveAllowed: Boolean,
    val observedAt: String,
)
