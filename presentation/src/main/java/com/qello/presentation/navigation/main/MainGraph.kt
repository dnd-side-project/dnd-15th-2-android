package com.qello.presentation.navigation.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.qello.presentation.navigation.TopLevelBackStack
import com.qello.presentation.ui.screen.main.MainScreen
import com.qello.presentation.ui.screen.main.my.MyScreen
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
    val navigator = remember { TopLevelBackStack<MainNavKey>(MainNavKey.Main) }

    NavDisplay(
        backStack = navigator.backStack,
        onBack = { navigator.removeLast() },
        entryProvider = entryProvider {
            entry<MainNavKey.Main> {
                MainScreen(
                    onNavigateToQuestionCompose = { navigator.add(MainNavKey.QuestionCompose) },
                    onNavigateToNotification = { navigator.add(MainNavKey.Notification) },
                    onNavigateToReceivedQuestion = { navigator.addTopLevel(MainNavKey.ReceivedQuestionList) },
                    onNavigateToSentQuestion = { navigator.addTopLevel(MainNavKey.SentQuestionList) },
                    onNavigateToMy = { navigator.add(MainNavKey.My) },
                )
            }
            entry<MainNavKey.QuestionCompose> {
                QuestionComposeScreen(
                    onNext = { navigator.add(MainNavKey.QuestionDirection) },
                    onNavigateToSuggest = { navigator.add(MainNavKey.QuestionSuggestCompose) },
                )
            }
            entry<MainNavKey.QuestionDirection> {
                QuestionDirectionScreen(
                    onSendComplete = {
                        navigator.add(MainNavKey.QuestionComplete(primaryButtonText = "새 질문 보내기"))
                    },
                )
            }
            entry<MainNavKey.QuestionSuggestCompose> {
                QuestionSuggestComposeScreen(
                    onSendComplete = {
                        navigator.add(MainNavKey.QuestionComplete(primaryButtonText = "재설문 보러가기"))
                    },
                )
            }
            entry<MainNavKey.Notification> { NotificationScreen() }
            entry<MainNavKey.ReceivedQuestionList> {
                ReceivedQuestionListScreen(
                    onItemClick = { id -> navigator.add(MainNavKey.ReceivedQuestionDetail(questionId = id)) },
                    onNavigateToSentQuestion = { navigator.addTopLevel(MainNavKey.SentQuestionList) },
                    onNavigateHome = { navigator.addTopLevel(MainNavKey.Main) },
                )
            }
            entry<MainNavKey.ReceivedQuestionDetail> { key ->
                ReceivedQuestionDetailScreen(questionId = key.questionId)
            }
            entry<MainNavKey.SentQuestionList> {
                SentQuestionListScreen(
                    onItemClick = { id -> navigator.add(MainNavKey.SentQuestionDetail(questionId = id)) },
                    onNavigateToReceivedQuestion = { navigator.addTopLevel(MainNavKey.ReceivedQuestionList) },
                    onNavigateHome = { navigator.addTopLevel(MainNavKey.Main) },
                )
            }
            entry<MainNavKey.SentQuestionDetail> { key ->
                SentQuestionDetailScreen(questionId = key.questionId)
            }
            entry<MainNavKey.My> { MyScreen() }
            entry<MainNavKey.QuestionComplete> { key ->
                QuestionCompleteScreen(
                    primaryButtonText = key.primaryButtonText,
                    onSendAnother = {
                        navigator.popToTopLevelStart()
                        navigator.add(MainNavKey.QuestionCompose)
                    },
                    onNavigateHome = { navigator.popToTopLevelStart() },
                )
            }
        },
    )
}
