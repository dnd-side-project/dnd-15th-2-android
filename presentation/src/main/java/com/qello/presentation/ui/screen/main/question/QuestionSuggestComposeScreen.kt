package com.qello.presentation.ui.screen.main.question

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qello.domain.validation.QuestionProposalError
import com.qello.domain.validation.QuestionProposalValidator
import com.qello.presentation.R
import com.qello.presentation.component.button.QelloBackButton
import com.qello.presentation.component.button.QelloLargeButton
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.component.text.QelloTextArea
import com.qello.presentation.ui.designsystem.theme.QelloTheme

@Composable
fun QuestionSuggestComposeScreen(
    onBack: () -> Unit,
    onSendComplete: () -> Unit,
    showSnackbar: suspend (message: String) -> Unit,
    viewModel: QuestionSuggestComposeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentOnSendComplete by rememberUpdatedState(onSendComplete)

    val resources = LocalResources.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                QuestionSuggestComposeSideEffect.NavigateToComplete -> currentOnSendComplete()
                is QuestionSuggestComposeSideEffect.ShowSnackbar -> showSnackbar(resources.getString(effect.message))
            }
        }
    }

    if (uiState.isSubmitting) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(QelloTheme.gradient.backgroundStrong)
                .padding(horizontal = QelloTheme.spacing.spacing20),
        ) {
            Spacer(Modifier.height(QelloTheme.spacing.spacing64))

            QelloText(
                text = "질문을 보내고 있어요!",
                style = QelloTheme.typography.heading1,
                color = QelloTheme.colors.label.strong,
            )
            QelloText(
                text = "잠시만 기다려주세요.",
                style = QelloTheme.typography.heading1,
                color = QelloTheme.colors.label.strong,
            )

            Spacer(Modifier.height(QelloTheme.spacing.spacing8))

            QelloText(
                text = "켈로에서 많은 사람들과 질문하며 알아가요",
                style = QelloTheme.typography.caption1,
                color = QelloTheme.colors.primary.normal,
            )
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(QelloTheme.gradient.backgroundStrong),
        ) {
            Box(modifier = Modifier.padding(start = 18.dp, top = 24.dp)) {
                QelloBackButton(onClick = onBack)
            }

            Spacer(Modifier.height(QelloTheme.spacing.spacing16))

            Column(modifier = Modifier.padding(horizontal = QelloTheme.spacing.spacing20)) {
                QelloText(
                    text = "어떤 질문을",
                    style = QelloTheme.typography.heading1,
                    color = QelloTheme.colors.label.strong,
                )
                QelloText(
                    text = "제안하고 싶나요?",
                    style = QelloTheme.typography.heading1,
                    color = QelloTheme.colors.label.strong,
                )

                Spacer(Modifier.height(QelloTheme.spacing.spacing24))

                QelloTextArea(
                    value = uiState.proposedText,
                    onValueChange = viewModel::onProposedTextChanged,
                    maxLength = QuestionProposalValidator.MAX_LENGTH,
                    isError = uiState.proposalError != null,
                    supportingText = uiState.proposalError?.message(),
                )

                Spacer(Modifier.height(QelloTheme.spacing.spacing20))

                QelloLargeButton(
                    text = "질문 제안하기",
                    onClick = viewModel::onSubmitClick,
                )
            }
        }
    }
}

@Composable
private fun QuestionProposalError.message(): String = when (this) {
    QuestionProposalError.BLANK -> stringResource(R.string.question_suggest_error_blank)
    QuestionProposalError.OUT_OF_LENGTH -> stringResource(
        R.string.question_suggest_error_out_of_length,
        QuestionProposalValidator.MAX_LENGTH,
    )
}
