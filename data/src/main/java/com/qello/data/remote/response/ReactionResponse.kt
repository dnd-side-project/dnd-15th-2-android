package com.qello.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class ReactionResponse(
    val reacted: Boolean,
    val reactionCount: Long,
)
