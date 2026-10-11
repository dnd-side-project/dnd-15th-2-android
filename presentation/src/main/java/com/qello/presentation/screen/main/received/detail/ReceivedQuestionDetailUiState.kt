package com.qello.presentation.screen.main.received.detail

import com.qello.presentation.model.AnswerUiModel
import com.qello.presentation.model.InboxCardUiModel

data class ReceivedQuestionDetailUiState(
    val card: InboxCardUiModel? = null,
    val answers: List<AnswerUiModel> = emptyList(),
    val isLoading: Boolean = false,
)
