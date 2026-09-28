package com.qello.presentation.ui.screen.main.received

sealed interface ReceivedQuestionDetailSideEffect {
    data class ShowSnackbar(val message: Int) : ReceivedQuestionDetailSideEffect
    data object AnswerSubmitted : ReceivedQuestionDetailSideEffect
    data object ReportSubmitted : ReceivedQuestionDetailSideEffect
    data object PostHidden : ReceivedQuestionDetailSideEffect
    data object HideUndone : ReceivedQuestionDetailSideEffect
}
