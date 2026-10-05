package com.qello.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class RecommendedQuestionResponse(
    val approvedQuestionId: Long,
    val questionText: String,
    val answerFormat: String,
)
