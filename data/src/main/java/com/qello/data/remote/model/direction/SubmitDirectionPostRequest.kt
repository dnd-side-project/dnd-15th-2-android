package com.qello.data.remote.model.direction

import kotlinx.serialization.Serializable

@Serializable
data class SubmitDirectionPostRequest(
    val approvedQuestionId: Long,
    val schemeId: Long,
    val segmentKey: String,
    val bodyText: String? = null,
    val mediaIds: List<Long> = emptyList(),
)
