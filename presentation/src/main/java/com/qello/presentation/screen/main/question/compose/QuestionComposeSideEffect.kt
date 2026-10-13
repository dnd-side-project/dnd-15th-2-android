package com.qello.presentation.screen.main.question.compose

sealed interface QuestionComposeSideEffect {
    data class ShowSnackbar(val message: Int) : QuestionComposeSideEffect
}
