package com.qello.presentation.ui.screen.main.question

sealed interface QuestionDirectionSideEffect {
    data object NavigateToComplete : QuestionDirectionSideEffect
    data class ShowSnackbar(val message: Int) : QuestionDirectionSideEffect
}
