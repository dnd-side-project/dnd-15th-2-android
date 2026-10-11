package com.qello.presentation.screen.main.question.suggest

import com.qello.presentation.model.QuestionProposalUiModel

data class QuestionSuggestListUiState(
    val proposals: List<QuestionProposalUiModel> = emptyList(),
    val isLoading: Boolean = false,
)
