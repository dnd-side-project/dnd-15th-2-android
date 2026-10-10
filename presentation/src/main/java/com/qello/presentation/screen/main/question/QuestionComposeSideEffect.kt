package com.qello.presentation.screen.main.question

sealed interface QuestionComposeSideEffect {
    data class ShowSnackbar(val message: Int) : QuestionComposeSideEffect
}
