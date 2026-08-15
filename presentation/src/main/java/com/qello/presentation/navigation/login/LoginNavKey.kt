package com.qello.presentation.navigation.login

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface LoginNavKey : NavKey {
    @Serializable
    data object Login : LoginNavKey

    @Serializable
    data object Nickname : LoginNavKey
}
