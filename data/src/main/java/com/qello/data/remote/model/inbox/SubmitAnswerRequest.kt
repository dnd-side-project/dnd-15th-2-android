package com.qello.data.remote.model.inbox

import kotlinx.serialization.Serializable

@Serializable
data class SubmitAnswerRequest(
    val bodyText: String,
    val mediaIds: List<Long> = emptyList(),
)
