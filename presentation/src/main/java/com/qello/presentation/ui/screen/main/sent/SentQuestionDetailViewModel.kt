package com.qello.presentation.ui.screen.main.sent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qello.domain.model.Answer
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
import java.time.Instant
import javax.inject.Inject

// TODO: 질문 보내기로 실제 보낸 질문 데이터가 쌓이면 목데이터 대신 실제 API 응답을 쓰도록 지운다.
private const val USE_MOCK_SENT_POST_ANSWERS = true

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
        if (USE_MOCK_SENT_POST_ANSWERS) {
            val answerCount = uiStateFlow.value.detail?.card?.answerCount ?: 0
            uiStateFlow.value = uiStateFlow.value.copy(answers = mockAnswers(postId, answerCount))
            return
        }

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

    private fun mockAnswers(postId: Long, count: Long): List<Answer> = (1..count).map { index ->
        Answer(
            answerId = postId * 100 + index,
            authorNickname = "익명$index",
            authorCoarseRegionCode = "미국 뉴욕",
            bodyText = "목데이터 답변 $index 입니다.",
            mediaIds = emptyList(),
            bearingFromSenderDegrees = 0.0,
            distanceM = index * 1000,
            distanceBand = null,
            publishedAt = Instant.now().minusSeconds(index * 1800L).toString(),
            editedAt = null,
            reactedByMe = index % 2 == 0L,
            reactionCount = index * 3,
        )
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
