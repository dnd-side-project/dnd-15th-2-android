package com.qello.presentation.screen.main.received.detail

sealed interface ReceivedQuestionDetailSideEffect {
    data class ShowSnackbar(val message: Int) : ReceivedQuestionDetailSideEffect
    data object AnswerSubmitted : ReceivedQuestionDetailSideEffect
    data object ReportSubmitted : ReceivedQuestionDetailSideEffect
    data object PostHidden : ReceivedQuestionDetailSideEffect
    data object HideUndone : ReceivedQuestionDetailSideEffect
}
