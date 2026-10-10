package com.qello.presentation.screen.main.share

sealed interface QuestionShareSideEffect {
    data class ShowSnackbar(val message: Int) : QuestionShareSideEffect
}
