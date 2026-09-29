package com.qello.presentation.ui.screen.main.share

import com.qello.domain.model.Answer

data class QuestionShareUiState(
    val questionText: String = "",
    val bodyText: String? = null,
    val mediaIds: List<Long> = emptyList(),
    val answers: List<Answer> = emptyList(),
    val isLoading: Boolean = false,
)
