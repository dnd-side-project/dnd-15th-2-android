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
    data object QuestionDirection : MainNavKey

    @Serializable
    data object QuestionSuggestCompose : MainNavKey

    @Serializable
    data class QuestionComplete(val primaryButtonText: String) : MainNavKey

    @Serializable
    data object Notification : MainNavKey
}
