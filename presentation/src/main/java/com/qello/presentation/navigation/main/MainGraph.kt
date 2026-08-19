package com.qello.presentation.navigation.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
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
        // 기본 크로스페이드는 전환 중간에 두 화면이 동시에 반투명해지면서 뒤쪽 배경이 비쳐 깜빡임처럼 보임.
        // 새 화면이 위에서 덮고, 이전 화면은 그대로 있다가 뒤로가기 때만 걷히도록 해서 배경이 비치지 않게 함.
        transitionSpec = { fadeIn(animationSpec = tween(300)) togetherWith ExitTransition.None },
        popTransitionSpec = { EnterTransition.None togetherWith fadeOut(animationSpec = tween(300)) },
        entryProvider = entryProvider {
            entry<MainNavKey.Main> {
                MainScreen(
                    onNavigateToQuestionCompose = { mainBackStack.add(MainNavKey.QuestionCompose) },
                    onNavigateToNotification = { mainBackStack.add(MainNavKey.Notification) },
                    onNavigateToReceivedQuestion = { mainBackStack.add(MainNavKey.ReceivedQuestionList) },
                    onNavigateToSentQuestion = { mainBackStack.add(MainNavKey.SentQuestionList) },
                    onNavigateToMy = { mainBackStack.add(MainNavKey.My) },
                )
            }

            entry<MainNavKey.QuestionCompose> {
                QuestionComposeScreen(
                    onBack = { mainBackStack.removeLastOrNull() },
                    onNext = { mainBackStack.add(MainNavKey.QuestionDirection) },
                    onNavigateToSuggest = { mainBackStack.add(MainNavKey.QuestionSuggestCompose) },
                )
            }

            entry<MainNavKey.QuestionDirection> {
                QuestionDirectionScreen(
                    onBack = { mainBackStack.removeLastOrNull() },
                    onSendComplete = {
                        mainBackStack.add(MainNavKey.QuestionComplete(primaryButtonText = "내 질문 보러 가기"))
                    },
                )
            }

            entry<MainNavKey.QuestionSuggestCompose> {
                QuestionSuggestComposeScreen(
                    onBack = { mainBackStack.removeLastOrNull() },
                    onSendComplete = {
                        mainBackStack.add(MainNavKey.QuestionComplete(primaryButtonText = "재설문 보러가기"))
                    },
                )
            }

            entry<MainNavKey.Notification> {
                NotificationScreen(
                    onBack = { mainBackStack.removeLastOrNull() },
                )
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

            entry<MainNavKey.My> {
                MyScreen()
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
