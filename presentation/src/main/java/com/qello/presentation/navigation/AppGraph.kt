package com.qello.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppGraph : NavKey {
    @Serializable
    data object Splash : AppGraph

    @Serializable
    data object Login : AppGraph

    @Serializable
    data object Main : AppGraph
}
