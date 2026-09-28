package com.qello.presentation.ui.screen.main.received

sealed interface ReceivedQuestionDetailSideEffect {
    data class ShowSnackbar(val message: Int) : ReceivedQuestionDetailSideEffect
}
