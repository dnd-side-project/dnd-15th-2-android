package com.qello.data.remote.model.direction

import kotlinx.serialization.Serializable

@Serializable
data class UpdatePresenceResponse(
    val applied: Boolean,
)
