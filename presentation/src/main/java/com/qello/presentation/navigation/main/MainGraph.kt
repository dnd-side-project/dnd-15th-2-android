package com.qello.presentation.navigation.main

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.qello.presentation.ui.screen.main.MainScreen
import com.qello.presentation.ui.screen.main.question.QuestionCompleteScreen
import com.qello.presentation.ui.screen.main.question.QuestionComposeScreen
import com.qello.presentation.ui.screen.main.question.QuestionDirectionScreen

@Composable
fun MainGraph() {
    val mainBackStack = rememberNavBackStack(MainNavKey.Main)

    fun popToMain() {
        while (mainBackStack.size > 1) {
            mainBackStack.removeLastOrNull()
        }
    }

    NavDisplay(
        backStack = mainBackStack,
        onBack = {
            if (mainBackStack.size > 1) {
                mainBackStack.removeLastOrNull()
            }
        },
        entryProvider = entryProvider {
            entry<MainNavKey.Main> {
                MainScreen(
                    onNavigateToQuestionCompose = { mainBackStack.add(MainNavKey.QuestionCompose) },
                )
            }

            entry<MainNavKey.QuestionCompose> {
                QuestionComposeScreen(
                    onNext = { mainBackStack.add(MainNavKey.QuestionDirection) },
                )
            }

            entry<MainNavKey.QuestionDirection> {
                QuestionDirectionScreen(
                    onSendComplete = { mainBackStack.add(MainNavKey.QuestionComplete) },
                )
            }

            entry<MainNavKey.QuestionComplete> {
                QuestionCompleteScreen(
                    onSendAnother = {
                        popToMain()
                        mainBackStack.add(MainNavKey.QuestionCompose)
                    },
                    onNavigateHome = { popToMain() },
                )
            }
        },
    )
}
