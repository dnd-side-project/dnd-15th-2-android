package com.qello.presentation.ui.screen.main.question

sealed interface QuestionComposeSideEffect {
    data class ShowSnackbar(val message: Int) : QuestionComposeSideEffect
}
