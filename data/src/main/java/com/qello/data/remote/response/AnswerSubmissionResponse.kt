package com.qello.data.remote.response

import kotlinx.serialization.Serializable

@Serializable
data class AnswerSubmissionResponse(
    val answerId: Long,
    val submissionStatus: String,
    val submittedAt: String,
)
