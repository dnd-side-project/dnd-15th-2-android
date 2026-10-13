package com.qello.presentation.screen.main.sent.list

import com.qello.presentation.model.SentPostCardUiModel

data class SentQuestionListUiState(
    val cards: List<SentPostCardUiModel> = emptyList(),
    val answeredOnly: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
)
