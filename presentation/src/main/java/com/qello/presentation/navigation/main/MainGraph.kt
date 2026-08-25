package com.qello.presentation.navigation.main

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.qello.presentation.R
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
                        navigator.add(
                            MainNavKey.QuestionComplete(
                                titleLine1 = "동쪽으로 질문을 보냈어요!",
                                titleLine2 = "곧 답변이 도착할 거예요",
                                caption = "켈로에서 많은 사람들과 질문하며 알아가요",
                                primaryButtonText = "내 질문 보러 가기",
                                retryDestination = MainNavKey.QuestionCompose,
                            ),
                        )
                    },
                )
            }

            entry<MainNavKey.QuestionSuggestCompose> {
                val goHomeButtonText = stringResource(R.string.question_suggest_go_home_button)
                val goSendButtonText = stringResource(R.string.question_suggest_go_send_button)

                QuestionSuggestComposeScreen(
                    onBack = { navigator.removeLast() },
                    onSendComplete = {
                        navigator.add(
                            MainNavKey.QuestionComplete(
                                titleLine1 = "질문 제안을 보냈어요!",
                                titleLine2 = "곧 검토가 진행될 거예요",
                                caption = "검토가 완료되면 알림을 드릴게요!",
                                primaryButtonText = goHomeButtonText,
                                secondaryButtonText = goSendButtonText,
                                secondaryDestination = MainNavKey.QuestionCompose,
                            ),
                        )
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
                QuestionCompleteScreen(
                    titleLine1 = key.titleLine1,
                    titleLine2 = key.titleLine2,
                    caption = key.caption,
                    primaryButtonText = key.primaryButtonText,
                    secondaryButtonText = key.secondaryButtonText ?: stringResource(R.string.navigate_home_button),
                    onSendAnother = {
                        navigator.popToTopLevelStart()
                        key.retryDestination?.let { navigator.add(it) }
                    },
                    onNavigateHome = {
                        navigator.popToTopLevelStart()
                        key.secondaryDestination?.let { navigator.add(it) }
                    },
                )
            }
        },
    )
}
