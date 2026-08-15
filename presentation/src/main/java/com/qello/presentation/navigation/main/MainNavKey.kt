package com.qello.presentation.navigation.main

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface MainNavKey : NavKey {
    @Serializable
    data object Main : MainNavKey
}
