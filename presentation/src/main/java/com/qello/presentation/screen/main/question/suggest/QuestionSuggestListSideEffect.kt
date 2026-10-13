package com.qello.presentation.screen.main.question.suggest

sealed interface QuestionSuggestListSideEffect {
    data class ShowSnackbar(val message: Int) : QuestionSuggestListSideEffect
}
