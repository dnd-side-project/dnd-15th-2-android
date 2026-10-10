package com.qello.presentation.screen.main.question.suggest

import com.qello.domain.model.QuestionProposal

data class QuestionSuggestListUiState(
    val proposals: List<QuestionProposal> = emptyList(),
    val isLoading: Boolean = false,
)
