package com.qello.presentation.ui.screen.main.sent

import com.qello.domain.model.SentPostCard

data class SentQuestionListUiState(
    val cards: List<SentPostCard> = emptyList(),
    val answeredOnly: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
)
