package com.qello.presentation.ui.screen.main.share

sealed interface QuestionShareSideEffect {
    data class ShowSnackbar(val message: Int) : QuestionShareSideEffect
}
