package com.qello.data.remote.model.direction

import kotlinx.serialization.Serializable

@Serializable
data class ReactionResponse(
    val reacted: Boolean,
    val reactionCount: Long,
)
