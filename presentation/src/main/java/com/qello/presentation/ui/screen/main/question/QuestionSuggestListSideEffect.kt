package com.qello.presentation.ui.screen.main.question

sealed interface QuestionSuggestListSideEffect {
    data class ShowSnackbar(val message: Int) : QuestionSuggestListSideEffect
}
