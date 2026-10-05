package com.qello.presentation.ui.screen.main.sent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qello.domain.repository.DirectionRepository
import com.qello.domain.repository.SentPostRepository
import com.qello.domain.result.AppResult
import com.qello.domain.result.asResult
import com.qello.presentation.common.toMessageRes
import com.qello.presentation.ui.screen.main.share.QuestionShareSideEffect
import com.qello.presentation.ui.screen.main.share.QuestionShareUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SentQuestionShareViewModel @Inject constructor(
    private val sentPostRepository: SentPostRepository,
    private val directionRepository: DirectionRepository,
) : ViewModel() {
    private val uiStateFlow = MutableStateFlow(QuestionShareUiState())
    val uiState: StateFlow<QuestionShareUiState> = uiStateFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = QuestionShareUiState(),
    )

    private val _sideEffect = Channel<QuestionShareSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<QuestionShareSideEffect> = _sideEffect.receiveAsFlow()

    private var loadedPostId: Long? = null

    fun load(postId: Long) {
        if (loadedPostId == postId) return
        loadedPostId = postId

        suspend { sentPostRepository.getSentPostDetail(postId) }
            .asFlow()
            .asResult()
            .onEach { result ->
                when (result) {
                    AppResult.Loading -> uiStateFlow.value = uiStateFlow.value.copy(isLoading = true)

                    is AppResult.Success -> {
                        val card = result.data.card
                        uiStateFlow.value = QuestionShareUiState(
                            questionText = card.questionText,
                            bodyText = card.bodyText,
                            media = card.media,
                            isLoading = false,
                        )
                        loadAnswers(postId)
                    }

                    is AppResult.Error -> {
                        uiStateFlow.value = uiStateFlow.value.copy(isLoading = false)
                        _sideEffect.send(QuestionShareSideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadAnswers(postId: Long) {
        suspend { directionRepository.getAnswers(postId) }
            .asFlow()
            .asResult()
            .onEach { result ->
                when (result) {
                    AppResult.Loading -> Unit

                    is AppResult.Success -> uiStateFlow.value = uiStateFlow.value.copy(answers = result.data)

                    is AppResult.Error -> {
                        _sideEffect.send(QuestionShareSideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
