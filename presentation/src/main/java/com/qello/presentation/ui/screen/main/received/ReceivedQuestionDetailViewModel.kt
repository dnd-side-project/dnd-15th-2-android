package com.qello.presentation.ui.screen.main.received

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qello.domain.model.Answer
import com.qello.domain.model.InboxCard
import com.qello.domain.repository.DirectionRepository
import com.qello.domain.repository.InboxRepository
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
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ReceivedQuestionDetailViewModel @Inject constructor(
    private val inboxRepository: InboxRepository,
    private val directionRepository: DirectionRepository,
) : ViewModel() {
    private val uiStateFlow = MutableStateFlow(ReceivedQuestionDetailUiState())
    val uiState: StateFlow<ReceivedQuestionDetailUiState> = uiStateFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = ReceivedQuestionDetailUiState(),
    )

    private val _sideEffect = Channel<ReceivedQuestionDetailSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<ReceivedQuestionDetailSideEffect> = _sideEffect.receiveAsFlow()

    private var loadedPostRecipientId: Long? = null

    fun load(postRecipientId: Long) {
        if (loadedPostRecipientId == postRecipientId) return
        loadedPostRecipientId = postRecipientId

        suspend { inboxRepository.getInboxDetail(postRecipientId) }
            .asFlow()
            .asResult()
            .onEach { result ->
                when (result) {
                    AppResult.Loading -> uiStateFlow.value = uiStateFlow.value.copy(isLoading = true)

                    is AppResult.Success -> {
                        uiStateFlow.value = ReceivedQuestionDetailUiState(detail = result.data, isLoading = false)
                        loadAnswers(result.data.card.postId)
                    }

                    is AppResult.Error -> {
                        uiStateFlow.value = uiStateFlow.value.copy(isLoading = false)
                        _sideEffect.send(ReceivedQuestionDetailSideEffect.ShowSnackbar(result.error.toMessageRes()))
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
                        _sideEffect.send(ReceivedQuestionDetailSideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAnswerSubmit(bodyText: String) {
        if (bodyText.isBlank()) return
        val postRecipientId = loadedPostRecipientId ?: return

        suspend {
            inboxRepository.submitAnswer(
                postRecipientId = postRecipientId,
                bodyText = bodyText,
                idempotencyKey = UUID.randomUUID().toString(),
            )
        }
            .asFlow()
            .asResult()
            .onEach { result ->
                when (result) {
                    AppResult.Loading -> Unit

                    is AppResult.Success -> _sideEffect.send(ReceivedQuestionDetailSideEffect.AnswerSubmitted)

                    is AppResult.Error -> {
                        _sideEffect.send(ReceivedQuestionDetailSideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onReactionToggle() {
        val card = uiStateFlow.value.detail?.card ?: return
        val wasReacted = card.reactedByMe
        val postId = card.postId

        // 응답을 기다리지 않고 먼저 바꿔서 보여주고, 실패하면 원래 상태로 되돌린다
        updateCard(card.copy(reactedByMe = !wasReacted, reactionCount = card.reactionCount + if (wasReacted) -1 else 1))

        suspend {
            if (wasReacted) directionRepository.cancelPostReaction(postId) else directionRepository.reactToPost(postId)
        }
            .asFlow()
            .asResult()
            .onEach { result ->
                when (result) {
                    AppResult.Loading -> Unit

                    is AppResult.Success -> {
                        updateCard(card.copy(reactedByMe = result.data.reacted, reactionCount = result.data.reactionCount))
                    }

                    is AppResult.Error -> {
                        updateCard(card)
                        _sideEffect.send(ReceivedQuestionDetailSideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun updateCard(card: InboxCard) {
        val detail = uiStateFlow.value.detail ?: return
        uiStateFlow.value = uiStateFlow.value.copy(detail = detail.copy(card = card))
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
                        _sideEffect.send(ReceivedQuestionDetailSideEffect.ShowSnackbar(result.error.toMessageRes()))
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
