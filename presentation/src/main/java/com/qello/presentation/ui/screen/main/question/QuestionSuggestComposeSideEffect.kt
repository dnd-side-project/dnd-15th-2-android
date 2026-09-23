package com.qello.presentation.ui.screen.main.question

sealed interface QuestionSuggestComposeSideEffect {
    data object NavigateToComplete : QuestionSuggestComposeSideEffect
    data class ShowSnackbar(val message: Int) : QuestionSuggestComposeSideEffect
}
