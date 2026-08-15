package com.qello.presentation.navigation.login

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.qello.presentation.ui.screen.login.LoginScreen
import com.qello.presentation.ui.screen.login.NicknameScreen

@Composable
fun LoginGraph(
    onLoginFinished: () -> Unit,
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
                    onNavigateToMain = onLoginFinished,
                )
            }
        },
    )
}
