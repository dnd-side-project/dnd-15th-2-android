package com.qello.presentation.screen.main.question.suggest

sealed interface QuestionSuggestComposeSideEffect {
    data object NavigateToComplete : QuestionSuggestComposeSideEffect
    data class ShowSnackbar(val message: Int) : QuestionSuggestComposeSideEffect
}
