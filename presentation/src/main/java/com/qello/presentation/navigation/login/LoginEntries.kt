package com.qello.presentation.navigation.login

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.qello.presentation.navigation.Navigator
import com.qello.presentation.navigation.main.MainNavKey
import com.qello.presentation.ui.screen.login.LoginScreen
import com.qello.presentation.ui.screen.login.nickname.NicknameScreen
import com.qello.presentation.ui.screen.login.welcome.WelcomeScreen

fun EntryProviderScope<NavKey>.loginEntries(
    navigator: Navigator,
    showSnackbar: suspend (message: String) -> Unit,
) {
    entry<LoginNavKey.Login> {
        LoginScreen(
            onNavigateToNickname = { navigator.navigate(LoginNavKey.Nickname) },
        )
    }

    entry<LoginNavKey.Nickname> {
        NicknameScreen(
            onBackClick = { navigator.goBack() },
            onNavigateToWelcome = { nickname ->
                navigator.replaceCurrentStack(LoginNavKey.Welcome(nickname))
            },
            showSnackbar = showSnackbar,
        )
    }

    entry<LoginNavKey.Welcome> { key ->
        WelcomeScreen(
            nickname = key.nickname,
            onStartClick = { navigator.resetTo(MainNavKey.Main) },
        )
    }
}
