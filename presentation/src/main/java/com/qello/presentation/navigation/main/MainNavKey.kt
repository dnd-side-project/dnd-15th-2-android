package com.qello.presentation.navigation.main

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface MainNavKey : NavKey {
    @Serializable
    data object Main : MainNavKey

    @Serializable
    data object QuestionCompose : MainNavKey

    @Serializable
    data class QuestionDirection(
        val bodyText: String,
        val mediaId: Long?,
        val approvedQuestionId: Long,
    ) : MainNavKey

    @Serializable
    data object QuestionSuggestCompose : MainNavKey

    @Serializable
    data object QuestionSuggestList : MainNavKey

    @Serializable
    data class QuestionComplete(
        val type: QuestionCompleteType,
    ) : MainNavKey

    @Serializable
    data object Notification : MainNavKey

    @Serializable
    data object ReceivedQuestionList : MainNavKey

    @Serializable
    data class ReceivedQuestionDetail(val questionId: Int) : MainNavKey

    @Serializable
    data class ReceivedQuestionShare(val questionId: Int) : MainNavKey

    @Serializable
    data object SentQuestionList : MainNavKey

    @Serializable
    data class SentQuestionDetail(val questionId: Int) : MainNavKey

    @Serializable
    data class SentQuestionShare(val questionId: Int) : MainNavKey

    @Serializable
    data object My : MainNavKey
}
