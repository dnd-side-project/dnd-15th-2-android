package com.qello.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class DirectionPostSubmissionResponse(
    val postId: Long,
)
