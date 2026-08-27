package com.qello.presentation.navigation.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
        // 기본 크로스페이드는 전환 중간에 두 화면이 동시에 반투명해지면서 뒤쪽 배경이 비쳐 깜빡임처럼 보임.
        // 새 화면이 위에서 덮고, 이전 화면은 그대로 있다가 뒤로가기 때만 걷히도록 해서 배경이 비치지 않게 함.
        transitionSpec = { fadeIn(animationSpec = tween(300)) togetherWith ExitTransition.None },
        popTransitionSpec = { EnterTransition.None togetherWith fadeOut(animationSpec = tween(300)) },
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
                    onBack = { navigator.removeLast() },
                    onNext = { navigator.add(MainNavKey.QuestionDirection) },
                    onNavigateToSuggest = { navigator.add(MainNavKey.QuestionSuggestCompose) },
                )
            }

            entry<MainNavKey.QuestionDirection> {
                QuestionDirectionScreen(
                    onBack = { navigator.removeLast() },
                    onSendComplete = {
                        navigator.add(MainNavKey.QuestionComplete(type = QuestionCompleteType.SEND_QUESTION))
                    },
                )
            }

            entry<MainNavKey.QuestionSuggestCompose> {
                QuestionSuggestComposeScreen(
                    onBack = { navigator.removeLast() },
                    onSendComplete = {
                        navigator.add(MainNavKey.QuestionComplete(type = QuestionCompleteType.SUGGEST_QUESTION))
                    },
                )
            }

            entry<MainNavKey.Notification> {
                NotificationScreen(
                    onBack = { navigator.removeLast() },
                    onNavigateToReceivedDetail = { id -> navigator.add(MainNavKey.ReceivedQuestionDetail(questionId = id)) },
                    onNavigateToSentDetail = { id -> navigator.add(MainNavKey.SentQuestionDetail(questionId = id)) },
                )
            }

            entry<MainNavKey.ReceivedQuestionList> {
                ReceivedQuestionListScreen(
                    onItemClick = { id -> navigator.add(MainNavKey.ReceivedQuestionDetail(questionId = id)) },
                    onNavigateToSentQuestion = { navigator.addTopLevel(MainNavKey.SentQuestionList) },
                    onNavigateToNotification = { navigator.add(MainNavKey.Notification) },
                    onNavigateHome = { navigator.addTopLevel(MainNavKey.Main) },
                )
            }

            entry<MainNavKey.ReceivedQuestionDetail> { key ->
                ReceivedQuestionDetailScreen(
                    questionId = key.questionId,
                    onBack = { navigator.removeLast() },
                )
            }

            entry<MainNavKey.SentQuestionList> {
                SentQuestionListScreen(
                    onItemClick = { id -> navigator.add(MainNavKey.SentQuestionDetail(questionId = id)) },
                    onNavigateToReceivedQuestion = { navigator.addTopLevel(MainNavKey.ReceivedQuestionList) },
                    onNavigateToNotification = { navigator.add(MainNavKey.Notification) },
                    onNavigateHome = { navigator.addTopLevel(MainNavKey.Main) },
                )
            }

            entry<MainNavKey.SentQuestionDetail> { key ->
                SentQuestionDetailScreen(
                    questionId = key.questionId,
                    onBack = { navigator.removeLast() },
                )
            }
            entry<MainNavKey.My> { MyScreen() }
            entry<MainNavKey.QuestionComplete> { key ->
                val content = key.type.toContent()

                QuestionCompleteScreen(
                    titleLine1 = content.titleLine1,
                    titleLine2 = content.titleLine2,
                    caption = content.caption,
                    primaryButtonText = content.primaryButtonText,
                    secondaryButtonText = content.secondaryButtonText,
                    onSendAnother = {
                        navigator.popToTopLevelStart()
                        content.retryDestination?.let { navigator.add(it) }
                    },
                    onNavigateHome = {
                        navigator.popToTopLevelStart()
                        content.secondaryDestination?.let { navigator.add(it) }
                    },
                )
            }
        },
    )
}
