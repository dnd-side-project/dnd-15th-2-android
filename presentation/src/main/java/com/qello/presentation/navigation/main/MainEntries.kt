package com.qello.presentation.navigation.main

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.qello.presentation.navigation.Navigator
import com.qello.presentation.ui.screen.main.MainScreen
import com.qello.presentation.ui.screen.main.my.MyScreen
import com.qello.presentation.ui.screen.main.notification.NotificationScreen
import com.qello.presentation.ui.screen.main.question.QuestionCompleteScreen
import com.qello.presentation.ui.screen.main.question.QuestionComposeScreen
import com.qello.presentation.ui.screen.main.question.QuestionDirectionScreen
import com.qello.presentation.ui.screen.main.question.QuestionSuggestComposeScreen
import com.qello.presentation.ui.screen.main.question.QuestionSuggestListScreen
import com.qello.presentation.ui.screen.main.received.ReceivedQuestionDetailScreen
import com.qello.presentation.ui.screen.main.received.ReceivedQuestionListScreen
import com.qello.presentation.ui.screen.main.sent.SentQuestionDetailScreen
import com.qello.presentation.ui.screen.main.sent.SentQuestionListScreen

fun EntryProviderScope<NavKey>.mainEntries(
    navigator: Navigator,
    showSnackbar: suspend (message: String) -> Unit,
) {
    entry<MainNavKey.Main> {
        MainScreen(
            onNavigateToQuestionCompose = { navigator.navigate(MainNavKey.QuestionCompose) },
            onNavigateToNotification = { navigator.navigate(MainNavKey.Notification) },
            onNavigateToReceivedQuestion = { navigator.navigate(MainNavKey.ReceivedQuestionList) },
            onNavigateToSentQuestion = { navigator.navigate(MainNavKey.SentQuestionList) },
            onNavigateToQuestionSuggestList = { navigator.navigate(MainNavKey.QuestionSuggestList) },
            onNavigateToMy = { navigator.navigate(MainNavKey.My) },
        )
    }

    entry<MainNavKey.QuestionCompose> {
        QuestionComposeScreen(
            onBack = { navigator.goBack() },
            onNext = { navigator.navigate(MainNavKey.QuestionDirection) },
            onNavigateToSuggest = { navigator.navigate(MainNavKey.QuestionSuggestCompose) },
        )
    }

    entry<MainNavKey.QuestionDirection> {
        QuestionDirectionScreen(
            onBack = { navigator.goBack() },
            onSendComplete = {
                navigator.navigate(MainNavKey.QuestionComplete(type = QuestionCompleteType.SEND_QUESTION))
            },
        )
    }

    entry<MainNavKey.QuestionSuggestCompose> {
        QuestionSuggestComposeScreen(
            onBack = { navigator.goBack() },
            onSendComplete = {
                navigator.navigate(MainNavKey.QuestionComplete(type = QuestionCompleteType.SUGGEST_QUESTION))
            },
            showSnackbar = showSnackbar,
        )
    }

    entry<MainNavKey.QuestionSuggestList> {
        QuestionSuggestListScreen(
            onBack = { navigator.goBack() },
            onNavigateToSuggestCompose = { navigator.navigate(MainNavKey.QuestionSuggestCompose) },
            showSnackbar = showSnackbar,
        )
    }

    entry<MainNavKey.Notification> {
        NotificationScreen(
            onBack = { navigator.goBack() },
            onNavigateToReceivedDetail = { id ->
                navigator.navigate(MainNavKey.ReceivedQuestionDetail(questionId = id))
            },
            onNavigateToSentDetail = { id ->
                navigator.navigate(MainNavKey.SentQuestionDetail(questionId = id))
            },
        )
    }

    entry<MainNavKey.ReceivedQuestionList> {
        ReceivedQuestionListScreen(
            onItemClick = { id -> navigator.navigate(MainNavKey.ReceivedQuestionDetail(questionId = id)) },
            onNavigateToSentQuestion = { navigator.navigate(MainNavKey.SentQuestionList) },
            onNavigateToNotification = { navigator.navigate(MainNavKey.Notification) },
            onNavigateHome = { navigator.navigate(MainNavKey.Main) },
        )
    }

    entry<MainNavKey.ReceivedQuestionDetail> { key ->
        ReceivedQuestionDetailScreen(
            questionId = key.questionId,
            onBack = { navigator.goBack() },
        )
    }

    entry<MainNavKey.SentQuestionList> {
        SentQuestionListScreen(
            onItemClick = { id -> navigator.navigate(MainNavKey.SentQuestionDetail(questionId = id)) },
            onNavigateToReceivedQuestion = { navigator.navigate(MainNavKey.ReceivedQuestionList) },
            onNavigateToNotification = { navigator.navigate(MainNavKey.Notification) },
            onNavigateHome = { navigator.navigate(MainNavKey.Main) },
        )
    }

    entry<MainNavKey.SentQuestionDetail> { key ->
        SentQuestionDetailScreen(
            questionId = key.questionId,
            onBack = { navigator.goBack() },
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
                navigator.navigate(MainNavKey.QuestionCompose)
            },
            onNavigateHome = { navigator.popToTopLevelStart() },
        )
    }
}
