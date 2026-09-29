package com.qello.presentation.ui.screen.main.sent

sealed interface SentQuestionListSideEffect {
    data class ShowSnackbar(val message: Int) : SentQuestionListSideEffect
}
