package com.qello.presentation.ui.screen.main.question

import com.qello.domain.validation.QuestionProposalError

data class QuestionSuggestComposeUiState(
    val proposedText: String = "",
    val proposalError: QuestionProposalError? = null,
    val isSubmitting: Boolean = false,
)
