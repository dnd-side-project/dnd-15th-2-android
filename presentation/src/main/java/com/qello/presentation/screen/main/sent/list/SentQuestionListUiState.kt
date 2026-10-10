package com.qello.presentation.screen.main.sent.list

import com.qello.domain.model.SentPostCard

data class SentQuestionListUiState(
    val cards: List<SentPostCard> = emptyList(),
    val answeredOnly: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
)
