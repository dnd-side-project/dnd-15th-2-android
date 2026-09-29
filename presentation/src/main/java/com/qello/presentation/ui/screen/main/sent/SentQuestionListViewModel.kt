package com.qello.presentation.ui.screen.main.sent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qello.domain.model.SentPostCard
import com.qello.domain.model.SentPostCursor
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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SentQuestionListViewModel @Inject constructor(
    private val sentPostRepository: SentPostRepository,
) : ViewModel() {
    private val cards = MutableStateFlow(emptyList<SentPostCard>())
    private val answeredOnly = MutableStateFlow(false)
    private val isLoading = MutableStateFlow(false)
    private val isLoadingMore = MutableStateFlow(false)

    private var nextCursor: SentPostCursor? = null
    private var endReached = false

    val uiState: StateFlow<SentQuestionListUiState> = combine(
        cards,
        answeredOnly,
        isLoading,
        isLoadingMore,
    ) { cards, answeredOnly, isLoading, isLoadingMore ->
        SentQuestionListUiState(
            cards = cards,
            answeredOnly = answeredOnly,
            isLoading = isLoading,
            isLoadingMore = isLoadingMore,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = SentQuestionListUiState(),
    )

    private val _sideEffect = Channel<SentQuestionListSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<SentQuestionListSideEffect> = _sideEffect.receiveAsFlow()

    init {
        load()
    }

    fun onAnsweredOnlyToggled() {
        answeredOnly.value = !answeredOnly.value
    }

    fun loadMore() {
        if (isLoading.value || isLoadingMore.value || endReached) return
        val cursor = nextCursor ?: return

        suspend { sentPostRepository.getSentPosts(cursor = cursor) }
            .asFlow()
            .asResult()
            .onEach { result ->
                when (result) {
                    AppResult.Loading -> isLoadingMore.value = true

                    is AppResult.Success -> {
                        isLoadingMore.value = false
                        nextCursor = result.data.nextCursor
                        endReached = result.data.nextCursor == null
                        cards.value = cards.value + result.data.cards
                    }

                    is AppResult.Error -> {
                        isLoadingMore.value = false
                        _sideEffect.send(SentQuestionListSideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun load() {
        suspend { sentPostRepository.getSentPosts() }
            .asFlow()
            .asResult()
            .onEach { result ->
                when (result) {
                    AppResult.Loading -> isLoading.value = true

                    is AppResult.Success -> {
                        isLoading.value = false
                        nextCursor = result.data.nextCursor
                        endReached = result.data.nextCursor == null
                        cards.value = result.data.cards
                    }

                    is AppResult.Error -> {
                        isLoading.value = false
                        _sideEffect.send(SentQuestionListSideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
