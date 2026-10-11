package com.qello.presentation.screen.main.received.detail

import com.qello.domain.model.InboxDetail
import com.qello.presentation.model.AnswerUiModel

data class ReceivedQuestionDetailUiState(
    val detail: InboxDetail? = null,
    val answers: List<AnswerUiModel> = emptyList(),
    val isLoading: Boolean = false,
)
