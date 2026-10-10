package com.qello.presentation.screen.main.sent

sealed interface SentQuestionListSideEffect {
    data class ShowSnackbar(val message: Int) : SentQuestionListSideEffect
}
