package com.qello.presentation.ui.screen.main.share

import com.qello.domain.model.Answer
import com.qello.domain.model.FeedMedia

data class QuestionShareUiState(
    val questionText: String = "",
    val bodyText: String? = null,
    val media: List<FeedMedia> = emptyList(),
    val answers: List<Answer> = emptyList(),
    val isLoading: Boolean = false,
)
