package com.qello.presentation.navigation.main

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.qello.presentation.ui.screen.main.MainScreen

@Composable
fun MainGraph() {
    val mainBackStack = rememberNavBackStack(MainNavKey.Main)

    NavDisplay(
        backStack = mainBackStack,
        onBack = {
            if (mainBackStack.size > 1) {
                mainBackStack.removeLastOrNull()
            }
        },
        entryProvider = entryProvider {
            entry<MainNavKey.Main> {
                MainScreen()
            }
        },
    )
}
