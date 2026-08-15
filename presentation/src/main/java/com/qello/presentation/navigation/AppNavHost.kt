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
fun AppNavHost(modifier: Modifier = Modifier) {
    val rootBackStack = rememberNavBackStack(AppGraph.Splash)

    NavDisplay(
        modifier = modifier,
        backStack = rootBackStack,
        onBack = { rootBackStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<AppGraph.Splash> {
                SplashScreen(
                    onNavigateToLogin = { rootBackStack.add(AppGraph.Login) },
                    onNavigateToMain = { rootBackStack.add(AppGraph.Main) },
                )
            }
            entry<AppGraph.Login> {
                LoginGraph(
                    onLoginFinished = {
                        rootBackStack.removeLastOrNull()
                        rootBackStack.add(AppGraph.Main)
                    },
                )
            }
            entry<AppGraph.Main> {
                MainGraph()
            }
        },
    )
}
