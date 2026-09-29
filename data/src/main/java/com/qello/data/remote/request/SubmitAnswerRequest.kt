package com.qello.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class SubmitAnswerRequest(
    val bodyText: String,
    val mediaIds: List<Long> = emptyList(),
)
