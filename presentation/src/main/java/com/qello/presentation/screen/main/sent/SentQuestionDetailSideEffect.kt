package com.qello.presentation.screen.main.sent

sealed interface SentQuestionDetailSideEffect {
    data class ShowSnackbar(val message: Int) : SentQuestionDetailSideEffect
    data object ReportSubmitted : SentQuestionDetailSideEffect
}
