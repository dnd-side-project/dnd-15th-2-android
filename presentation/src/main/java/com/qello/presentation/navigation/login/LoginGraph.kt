package com.qello.presentation.navigation.login

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.qello.presentation.ui.screen.login.LoginScreen
import com.qello.presentation.ui.screen.login.nickname.NicknameScreen
import com.qello.presentation.ui.screen.login.welcome.WelcomeScreen

@Composable
fun LoginGraph(
    onLoginFinished: () -> Unit,
    showSnackbar: suspend (message: String) -> Unit,
) {
    val loginBackStack = rememberNavBackStack(LoginNavKey.Login)

    NavDisplay(
        backStack = loginBackStack,
        onBack = {
            if (loginBackStack.size > 1) {
                loginBackStack.removeLastOrNull()
            }
        },
        entryProvider = entryProvider {
            entry<LoginNavKey.Login> {
                LoginScreen(
                    onNavigateToNickname = { loginBackStack.add(LoginNavKey.Nickname) },
                )
            }

            entry<LoginNavKey.Nickname> {
                NicknameScreen(
                    onBackClick = { loginBackStack.removeLastOrNull() },
                    onNavigateToWelcome = { nickname ->
                        loginBackStack.clear()
                        loginBackStack.add(LoginNavKey.Welcome(nickname))
                    },
                    showSnackbar = showSnackbar,
                )
            }

            entry<LoginNavKey.Welcome> { key ->
                WelcomeScreen(
                    nickname = key.nickname,
                    onStartClick = onLoginFinished,
                )
            }
        },
    )
}
