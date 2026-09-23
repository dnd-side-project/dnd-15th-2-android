package com.qello.presentation.ui.screen.main.question

import com.qello.domain.model.QuestionProposal

data class QuestionSuggestListUiState(
    val proposals: List<QuestionProposal> = emptyList(),
    val isLoading: Boolean = false,
)
