package com.qello.data.remote.request

import kotlinx.serialization.Serializable

@Serializable
data class QuestionProposalRequest(
    val proposedText: String,
)
