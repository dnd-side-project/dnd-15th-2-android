package com.qello.presentation.screen.main.share

import com.qello.domain.model.FeedMedia
import com.qello.presentation.model.AnswerUiModel

data class QuestionShareUiState(
    val questionText: String = "",
    val bodyText: String? = null,
    val media: List<FeedMedia> = emptyList(),
    val answers: List<AnswerUiModel> = emptyList(),
    val isLoading: Boolean = false,
)
