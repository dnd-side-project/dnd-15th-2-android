package com.qello.presentation.screen.main.sent.detail

import com.qello.domain.model.SentPostDetail
import com.qello.presentation.model.AnswerUiModel

data class SentQuestionDetailUiState(
    val detail: SentPostDetail? = null,
    val answers: List<AnswerUiModel> = emptyList(),
    val isLoading: Boolean = false,
)
