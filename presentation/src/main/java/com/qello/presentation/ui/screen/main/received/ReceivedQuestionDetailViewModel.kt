package com.qello.presentation.ui.screen.main.received

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import javax.inject.Inject

@HiltViewModel
class ReceivedQuestionDetailViewModel @Inject constructor(
    private val inboxRepository: InboxRepository,
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
                    }

                    is AppResult.Error -> {
                        uiStateFlow.value = uiStateFlow.value.copy(isLoading = false)
                        _sideEffect.send(ReceivedQuestionDetailSideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
