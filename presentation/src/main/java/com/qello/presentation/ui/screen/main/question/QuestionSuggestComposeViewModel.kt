package com.qello.presentation.ui.screen.main.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qello.domain.repository.QuestionRepository
import com.qello.domain.result.AppResult
import com.qello.domain.result.asResult
import com.qello.domain.validation.QuestionProposalError
import com.qello.domain.validation.QuestionProposalValidator
import com.qello.presentation.common.toMessageRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class QuestionSuggestComposeViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
) : ViewModel() {
    private val proposedText = MutableStateFlow("")

    private val proposalError = MutableStateFlow<QuestionProposalError?>(null)

    private val isSubmitting = MutableStateFlow(false)

    val uiState: StateFlow<QuestionSuggestComposeUiState> = combine(
        proposedText,
        proposalError,
        isSubmitting,
    ) { proposedText, proposalError, isSubmitting ->
        QuestionSuggestComposeUiState(
            proposedText = proposedText,
            proposalError = proposalError,
            isSubmitting = isSubmitting,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = QuestionSuggestComposeUiState(),
    )

    private val _sideEffect = Channel<QuestionSuggestComposeSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<QuestionSuggestComposeSideEffect> = _sideEffect.receiveAsFlow()

    fun onProposedTextChanged(text: String) {
        proposedText.value = text
        proposalError.value = null
    }

    fun onSubmitClick() {
        val trimmedText = proposedText.value.trim()

        QuestionProposalValidator.validate(trimmedText)?.let { error ->
            proposalError.value = error
            return
        }

        suspend { questionRepository.submitQuestionProposal(trimmedText) }
            .asFlow()
            .asResult()
            .onEach { result ->
                when (result) {
                    AppResult.Loading -> isSubmitting.value = true

                    is AppResult.Success -> {
                        _sideEffect.send(QuestionSuggestComposeSideEffect.NavigateToComplete)
                        // 화면 전환(페이드)이 끝나기 전에 로딩 화면을 내리면 이전 화면이 잠깐 비쳐 깜빡이므로, 전환이 끝난 뒤에 내린다
                        delay(NAVIGATION_TRANSITION_MILLIS)
                        isSubmitting.value = false
                    }

                    is AppResult.Error -> {
                        isSubmitting.value = false
                        _sideEffect.send(QuestionSuggestComposeSideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
        const val NAVIGATION_TRANSITION_MILLIS = 500L
    }
}
