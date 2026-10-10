package com.qello.data.remote.model.question

import kotlinx.serialization.Serializable

@Serializable
data class QuestionProposalRequest(
    val proposedText: String,
)
