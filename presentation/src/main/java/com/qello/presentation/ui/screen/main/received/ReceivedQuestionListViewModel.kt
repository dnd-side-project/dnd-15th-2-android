package com.qello.presentation.ui.screen.main.received

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qello.domain.model.InboxCategory
import com.qello.domain.model.InboxListing
import com.qello.domain.model.ReportReason
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ReceivedQuestionListViewModel @Inject constructor(
    private val inboxRepository: InboxRepository,
    private val directionRepository: DirectionRepository,
) : ViewModel() {
    private val listing = MutableStateFlow(InboxListing(cards = emptyList(), chips = emptyList()))
    private val selectedSegmentKey = MutableStateFlow<String?>(null)
    private val answeredOnly = MutableStateFlow(false)
    private val isLoading = MutableStateFlow(false)

    val uiState: StateFlow<ReceivedQuestionListUiState> = combine(
        listing,
        selectedSegmentKey,
        answeredOnly,
        isLoading,
    ) { listing, selectedSegmentKey, answeredOnly, isLoading ->
        ReceivedQuestionListUiState(
            cards = listing.cards,
            chips = listing.chips,
            selectedSegmentKey = selectedSegmentKey,
            answeredOnly = answeredOnly,
            isLoading = isLoading,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = ReceivedQuestionListUiState(),
    )

    private val _sideEffect = Channel<ReceivedQuestionListSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<ReceivedQuestionListSideEffect> = _sideEffect.receiveAsFlow()

    init {
        load()
    }

    fun onSegmentSelected(segmentKey: String?) {
        if (selectedSegmentKey.value == segmentKey) return
        selectedSegmentKey.value = segmentKey
        load()
    }

    fun onAnsweredOnlyToggled() {
        answeredOnly.value = !answeredOnly.value
        load()
    }

    fun onReportSubmit(postId: Long, reasonCode: ReportReason) {
        val detail = if (reasonCode == ReportReason.OTHER) "기타" else null

        suspend { directionRepository.reportPost(postId, reasonCode, detail) }
            .asFlow()
            .asResult()
            .onEach { result ->
                when (result) {
                    AppResult.Loading -> Unit

                    is AppResult.Success -> _sideEffect.send(ReceivedQuestionListSideEffect.ReportSubmitted)

                    is AppResult.Error -> {
                        _sideEffect.send(ReceivedQuestionListSideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun load() {
        val category = if (answeredOnly.value) InboxCategory.ANSWERED else InboxCategory.UNANSWERED
        val segmentKey = selectedSegmentKey.value

        suspend { inboxRepository.getInbox(category, segmentKey) }
            .asFlow()
            .asResult()
            .onEach { result ->
                when (result) {
                    AppResult.Loading -> isLoading.value = true

                    is AppResult.Success -> {
                        isLoading.value = false
                        listing.value = result.data
                    }

                    is AppResult.Error -> {
                        isLoading.value = false
                        _sideEffect.send(ReceivedQuestionListSideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
