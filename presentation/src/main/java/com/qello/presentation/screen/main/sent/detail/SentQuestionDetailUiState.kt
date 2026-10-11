package com.qello.presentation.screen.main.sent.detail

import com.qello.presentation.model.AnswerUiModel
import com.qello.presentation.model.SentPostCardUiModel

data class SentQuestionDetailUiState(
    val card: SentPostCardUiModel? = null,
    val answers: List<AnswerUiModel> = emptyList(),
    val isLoading: Boolean = false,
)
