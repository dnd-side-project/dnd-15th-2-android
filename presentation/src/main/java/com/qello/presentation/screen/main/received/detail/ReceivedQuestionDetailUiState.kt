package com.qello.presentation.screen.main.received.detail

import com.qello.domain.model.Answer
import com.qello.domain.model.InboxDetail

data class ReceivedQuestionDetailUiState(
    val detail: InboxDetail? = null,
    val answers: List<Answer> = emptyList(),
    val isLoading: Boolean = false,
)
