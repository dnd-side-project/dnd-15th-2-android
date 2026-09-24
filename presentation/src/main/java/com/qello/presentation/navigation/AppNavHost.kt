package com.qello.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.qello.presentation.navigation.login.LoginNavKey
import com.qello.presentation.navigation.login.loginEntries
import com.qello.presentation.navigation.main.MainNavKey
import com.qello.presentation.navigation.main.mainEntries
import com.qello.presentation.ui.screen.splash.SplashScreen

private val TOP_LEVEL_KEYS: Set<NavKey> = setOf(
    AppNavKey.Splash,
    LoginNavKey.Login,
    MainNavKey.Main,
    MainNavKey.ReceivedQuestionList,
    MainNavKey.SentQuestionList,
)

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    showSnackbar: suspend (message: String) -> Unit,
) {
    val navigationState = rememberNavigationState(
        startKey = AppNavKey.Splash,
        topLevelKeys = TOP_LEVEL_KEYS,
    )
    val navigator = remember(navigationState) { Navigator(navigationState) }

    val entryProvider = entryProvider {
        entry<AppNavKey.Splash> {
            SplashScreen(
                onNavigateToLogin = { navigator.resetTo(LoginNavKey.Login) },
                onNavigateToMain = { navigator.resetTo(MainNavKey.Main) },
            )
        }

        loginEntries(navigator = navigator, showSnackbar = showSnackbar)

        mainEntries(navigator = navigator, showSnackbar = showSnackbar)
    }

    NavDisplay(
        entries = navigationState.toEntries(entryProvider),
        onBack = { navigator.goBack() },
        modifier = modifier,
        transitionSpec = { fadeIn(animationSpec = tween(300)) togetherWith ExitTransition.None },
        popTransitionSpec = { EnterTransition.None togetherWith fadeOut(animationSpec = tween(300)) },
    )
}
