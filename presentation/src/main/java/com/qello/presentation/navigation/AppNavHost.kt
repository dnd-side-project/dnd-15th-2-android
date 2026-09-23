package com.qello.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.qello.presentation.navigation.login.LoginGraph
import com.qello.presentation.navigation.main.MainGraph
import com.qello.presentation.ui.screen.splash.SplashScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    showSnackbar: suspend (message: String) -> Unit,
) {
    val rootBackStack = rememberNavBackStack(AppGraph.Splash)

    NavDisplay(
        modifier = modifier,
        backStack = rootBackStack,
        onBack = { rootBackStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<AppGraph.Splash> {
                // TODO: Nia의 Navigator 구현 하기
                SplashScreen(
                    onNavigateToLogin = {
                        rootBackStack.removeLastOrNull()
                        rootBackStack.add(AppGraph.Login)
                    },
                    onNavigateToMain = {
                        rootBackStack.removeLastOrNull()
                        rootBackStack.add(AppGraph.Main)
                    },
                )
            }
            entry<AppGraph.Login> {
                LoginGraph(
                    onLoginFinished = {
                        rootBackStack.removeLastOrNull()
                        rootBackStack.add(AppGraph.Main)
                    },
                    showSnackbar = showSnackbar,
                )
            }
            entry<AppGraph.Main> {
                MainGraph(
                    showSnackbar = showSnackbar,
                )
            }
        },
    )
}
