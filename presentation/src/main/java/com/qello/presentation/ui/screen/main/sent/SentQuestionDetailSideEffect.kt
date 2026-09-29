package com.qello.presentation.ui.screen.main.sent

sealed interface SentQuestionDetailSideEffect {
    data class ShowSnackbar(val message: Int) : SentQuestionDetailSideEffect
    data object ReportSubmitted : SentQuestionDetailSideEffect
}
