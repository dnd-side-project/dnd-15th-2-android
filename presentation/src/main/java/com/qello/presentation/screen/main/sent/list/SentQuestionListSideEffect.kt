package com.qello.presentation.screen.main.sent.list

sealed interface SentQuestionListSideEffect {
    data class ShowSnackbar(val message: Int) : SentQuestionListSideEffect
}
