package com.qello.presentation.ui.screen.main.question

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qello.domain.repository.QuestionRepository
import com.qello.domain.result.AppResult
import com.qello.domain.result.asResult
import com.qello.presentation.common.toMessageRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class QuestionSuggestListViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuestionSuggestListUiState())
    val uiState: StateFlow<QuestionSuggestListUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<QuestionSuggestListSideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<QuestionSuggestListSideEffect> = _sideEffect.receiveAsFlow()

    init {
        loadMyQuestionProposals()
    }

    fun loadMyQuestionProposals() {
        suspend { questionRepository.getMyQuestionProposals() }
            .asFlow()
            .asResult()
            .onEach { result ->
                when (result) {
                    AppResult.Loading -> _uiState.update { it.copy(isLoading = true) }

                    is AppResult.Success -> _uiState.update {
                        it.copy(isLoading = false, proposals = result.data)
                    }

                    is AppResult.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _sideEffect.send(QuestionSuggestListSideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}
