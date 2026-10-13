package com.qello.presentation.screen.main.question.suggest

import com.qello.domain.validation.QuestionProposalError

data class QuestionSuggestComposeUiState(
    val proposedText: String = "",
    val proposalError: QuestionProposalError? = null,
    val isSubmitting: Boolean = false,
)
