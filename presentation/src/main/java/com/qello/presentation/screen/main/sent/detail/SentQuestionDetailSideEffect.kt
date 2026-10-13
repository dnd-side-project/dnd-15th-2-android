package com.qello.presentation.screen.main.sent.detail

sealed interface SentQuestionDetailSideEffect {
    data class ShowSnackbar(val message: Int) : SentQuestionDetailSideEffect
    data object ReportSubmitted : SentQuestionDetailSideEffect
}
