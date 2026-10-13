package com.qello.presentation.screen.main.question.direction

sealed interface QuestionDirectionSideEffect {
    data object NavigateToComplete : QuestionDirectionSideEffect
    data class ShowSnackbar(val message: Int) : QuestionDirectionSideEffect
}
