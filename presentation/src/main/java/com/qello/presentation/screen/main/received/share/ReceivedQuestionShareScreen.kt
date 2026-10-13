package com.qello.presentation.screen.main.received.share

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalResources
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qello.presentation.screen.main.share.QuestionShareScreen
import com.qello.presentation.screen.main.share.QuestionShareSideEffect

@Composable
fun ReceivedQuestionShareScreen(
    questionId: Int,
    onBack: () -> Unit,
    showSnackbar: suspend (message: String) -> Unit,
    viewModel: ReceivedQuestionShareViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val resources = LocalResources.current

    LaunchedEffect(questionId) {
        viewModel.load(questionId.toLong())
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is QuestionShareSideEffect.ShowSnackbar -> showSnackbar(resources.getString(effect.message))
            }
        }
    }

    QuestionShareScreen(uiState = uiState, onBack = onBack)
}
