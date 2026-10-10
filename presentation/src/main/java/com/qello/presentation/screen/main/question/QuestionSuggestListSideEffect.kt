package com.qello.presentation.screen.main.question

sealed interface QuestionSuggestListSideEffect {
    data class ShowSnackbar(val message: Int) : QuestionSuggestListSideEffect
}
