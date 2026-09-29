package com.qello.presentation.ui.screen.main.received

sealed interface ReceivedQuestionListSideEffect {
    data class ShowSnackbar(val message: Int) : ReceivedQuestionListSideEffect
    data object ReportSubmitted : ReceivedQuestionListSideEffect
    data class PostHidden(val postRecipientId: Long) : ReceivedQuestionListSideEffect
    data object HideUndone : ReceivedQuestionListSideEffect
}
