package com.qello.presentation.ui.screen.main.sent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qello.domain.model.Answer
import com.qello.domain.model.ReportReason
import com.qello.domain.repository.DirectionRepository
import com.qello.domain.repository.SentPostRepository
import com.qello.domain.result.AppResult
import com.qello.domain.result.asResult
import com.qello.presentation.common.toMessageRes
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
class SentQuestionDetailViewModel @Inject constructor(
    private val sentPostRepository: SentPostRepository,
    private val directionRepository: DirectionRepository,
) : ViewModel() {
    private val uiStateFlow = MutableStateFlow(SentQuestionDetailUiState())
    val uiState: StateFlow<SentQuestionDetailUiState> = uiStateFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = SentQuestionDetailUiState(),
    )

    private val _sideEffect = Channel<SentQuestionDetailSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<SentQuestionDetailSideEffect> = _sideEffect.receiveAsFlow()

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
                        uiStateFlow.value = SentQuestionDetailUiState(detail = result.data, isLoading = false)
                        loadAnswers(postId)
                    }

                    is AppResult.Error -> {
                        uiStateFlow.value = uiStateFlow.value.copy(isLoading = false)
                        _sideEffect.send(SentQuestionDetailSideEffect.ShowSnackbar(result.error.toMessageRes()))
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
                        _sideEffect.send(SentQuestionDetailSideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAnswerReactionToggle(answerId: Long) {
        val answer = uiStateFlow.value.answers.firstOrNull { it.answerId == answerId } ?: return
        val wasReacted = answer.reactedByMe

        // 응답을 기다리지 않고 먼저 바꿔서 보여주고, 실패하면 원래 상태로 되돌린다
        updateAnswer(answer.copy(reactedByMe = !wasReacted, reactionCount = answer.reactionCount + if (wasReacted) -1 else 1))

        suspend {
            if (wasReacted) directionRepository.cancelAnswerReaction(answerId) else directionRepository.reactToAnswer(answerId)
        }
            .asFlow()
            .asResult()
            .onEach { result ->
                when (result) {
                    AppResult.Loading -> Unit

                    is AppResult.Success -> {
                        updateAnswer(answer.copy(reactedByMe = result.data.reacted, reactionCount = result.data.reactionCount))
                    }

                    is AppResult.Error -> {
                        updateAnswer(answer)
                        _sideEffect.send(SentQuestionDetailSideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAnswerReportSubmit(answerId: Long, reasonCode: ReportReason) {
        val detail = if (reasonCode == ReportReason.OTHER) "기타" else null

        suspend { directionRepository.reportAnswer(answerId, reasonCode, detail) }
            .asFlow()
            .asResult()
            .onEach { result ->
                when (result) {
                    AppResult.Loading -> Unit

                    is AppResult.Success -> _sideEffect.send(SentQuestionDetailSideEffect.ReportSubmitted)

                    is AppResult.Error -> {
                        _sideEffect.send(SentQuestionDetailSideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun updateAnswer(answer: Answer) {
        uiStateFlow.value = uiStateFlow.value.copy(
            answers = uiStateFlow.value.answers.map { if (it.answerId == answer.answerId) answer else it },
        )
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
