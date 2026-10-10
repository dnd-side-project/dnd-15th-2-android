package com.qello.presentation.screen.main.received.list

sealed interface ReceivedQuestionListSideEffect {
    data class ShowSnackbar(val message: Int) : ReceivedQuestionListSideEffect
    data object ReportSubmitted : ReceivedQuestionListSideEffect
    data class PostHidden(val postRecipientId: Long) : ReceivedQuestionListSideEffect
    data object HideUndone : ReceivedQuestionListSideEffect
}
