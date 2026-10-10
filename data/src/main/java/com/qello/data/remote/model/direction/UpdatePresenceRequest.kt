package com.qello.data.remote.model.direction

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePresenceRequest(
    val latitude: Double,
    val longitude: Double,
    val accuracyMeters: Double,
    val receiveAllowed: Boolean,
    val observedAt: String,
)
