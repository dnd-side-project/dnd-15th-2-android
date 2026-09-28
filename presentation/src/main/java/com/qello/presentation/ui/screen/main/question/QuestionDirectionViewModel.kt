package com.qello.presentation.ui.screen.main.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qello.domain.repository.DirectionRepository
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class QuestionDirectionViewModel @Inject constructor(
    private val directionRepository: DirectionRepository,
) : ViewModel() {
    private val isSending = MutableStateFlow(false)

    val uiState: StateFlow<QuestionDirectionUiState> = isSending
        .map { QuestionDirectionUiState(isSending = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = QuestionDirectionUiState(),
        )

    private val _sideEffect = Channel<QuestionDirectionSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<QuestionDirectionSideEffect> = _sideEffect.receiveAsFlow()

    fun onSendClick(bodyText: String, mediaId: Long?, segmentKey: String) {
        suspend {
            directionRepository.submitDirectionPost(
                // TODO: 승인된 질문 목록/할당 API가 생기면 실제 선택값으로 교체
                approvedQuestionId = APPROVED_QUESTION_ID_TEMP,
                // TODO: 방향 구획 체계가 여러 개 운영되면 실제 활성 스킴 조회로 교체
                schemeId = SCHEME_ID_TEMP,
                segmentKey = segmentKey,
                bodyText = bodyText.ifBlank { null },
                mediaIds = listOfNotNull(mediaId),
                idempotencyKey = UUID.randomUUID().toString(),
            )
        }
            .asFlow()
            .asResult()
            .onEach { result ->
                when (result) {
                    AppResult.Loading -> isSending.value = true

                    is AppResult.Success -> {
                        isSending.value = false
                        _sideEffect.send(QuestionDirectionSideEffect.NavigateToComplete)
                    }

                    is AppResult.Error -> {
                        isSending.value = false
                        _sideEffect.send(QuestionDirectionSideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
        const val APPROVED_QUESTION_ID_TEMP = 1L

        // OCTANT 스킴이 서버 DB에 하나만 시드돼 있어 사실상 고정값(V1 마이그레이션 기준)
        const val SCHEME_ID_TEMP = 1L
    }
}
