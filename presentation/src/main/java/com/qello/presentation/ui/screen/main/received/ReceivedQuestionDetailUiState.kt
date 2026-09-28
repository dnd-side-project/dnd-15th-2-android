package com.qello.presentation.ui.screen.main.received

import com.qello.domain.model.Answer
import com.qello.domain.model.InboxDetail

data class ReceivedQuestionDetailUiState(
    val detail: InboxDetail? = null,
    val answers: List<Answer> = emptyList(),
    val isLoading: Boolean = false,
)
