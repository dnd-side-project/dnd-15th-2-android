package com.qello.presentation.navigation.main

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.qello.presentation.ui.screen.main.MainScreen
import com.qello.presentation.ui.screen.main.notification.NotificationScreen
import com.qello.presentation.ui.screen.main.question.QuestionCompleteScreen
import com.qello.presentation.ui.screen.main.question.QuestionComposeScreen
import com.qello.presentation.ui.screen.main.question.QuestionDirectionScreen
import com.qello.presentation.ui.screen.main.question.QuestionSuggestComposeScreen
import com.qello.presentation.ui.screen.main.received.ReceivedQuestionDetailScreen
import com.qello.presentation.ui.screen.main.received.ReceivedQuestionListScreen
import com.qello.presentation.ui.screen.main.sent.SentQuestionDetailScreen
import com.qello.presentation.ui.screen.main.sent.SentQuestionListScreen

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
                    onNavigateToNotification = { mainBackStack.add(MainNavKey.Notification) },
                    onNavigateToReceivedQuestion = { mainBackStack.add(MainNavKey.ReceivedQuestionList) },
                    onNavigateToSentQuestion = { mainBackStack.add(MainNavKey.SentQuestionList) },
                )
            }

            entry<MainNavKey.QuestionCompose> {
                QuestionComposeScreen(
                    onNext = { mainBackStack.add(MainNavKey.QuestionDirection) },
                    onNavigateToSuggest = { mainBackStack.add(MainNavKey.QuestionSuggestCompose) },
                )
            }

            entry<MainNavKey.QuestionDirection> {
                QuestionDirectionScreen(
                    onSendComplete = {
                        mainBackStack.add(MainNavKey.QuestionComplete(primaryButtonText = "새 질문 보내기"))
                    },
                )
            }

            entry<MainNavKey.QuestionSuggestCompose> {
                QuestionSuggestComposeScreen(
                    onSendComplete = {
                        mainBackStack.add(MainNavKey.QuestionComplete(primaryButtonText = "재설문 보러가기"))
                    },
                )
            }

            entry<MainNavKey.Notification> {
                NotificationScreen()
            }

            entry<MainNavKey.ReceivedQuestionList> {
                ReceivedQuestionListScreen(
                    onItemClick = { id -> mainBackStack.add(MainNavKey.ReceivedQuestionDetail(questionId = id)) },
                )
            }

            entry<MainNavKey.ReceivedQuestionDetail> { key ->
                ReceivedQuestionDetailScreen(questionId = key.questionId)
            }

            entry<MainNavKey.SentQuestionList> {
                SentQuestionListScreen(
                    onItemClick = { id -> mainBackStack.add(MainNavKey.SentQuestionDetail(questionId = id)) },
                )
            }

            entry<MainNavKey.SentQuestionDetail> { key ->
                SentQuestionDetailScreen(questionId = key.questionId)
            }

            entry<MainNavKey.QuestionComplete> { key ->
                QuestionCompleteScreen(
                    primaryButtonText = key.primaryButtonText,
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
