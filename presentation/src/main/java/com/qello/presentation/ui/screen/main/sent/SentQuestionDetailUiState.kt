package com.qello.presentation.ui.screen.main.sent

import com.qello.domain.model.Answer
import com.qello.domain.model.SentPostDetail

data class SentQuestionDetailUiState(
    val detail: SentPostDetail? = null,
    val answers: List<Answer> = emptyList(),
    val isLoading: Boolean = false,
)
