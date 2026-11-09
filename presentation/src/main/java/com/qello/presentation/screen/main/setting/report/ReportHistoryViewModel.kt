package com.qello.presentation.screen.main.setting.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qello.domain.repository.ReportRepository
import com.qello.domain.result.AppResult
import com.qello.domain.result.asResult
import com.qello.presentation.mapper.toMessageRes
import com.qello.presentation.mapper.toUiModel
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
class ReportHistoryViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReportHistoryUiState())
    val uiState: StateFlow<ReportHistoryUiState> = _uiState.asStateFlow()

    private val _sideEffect = Channel<ReportHistorySideEffect>(Channel.BUFFERED)
    val sideEffect: Flow<ReportHistorySideEffect> = _sideEffect.receiveAsFlow()

    // null이면 마지막 페이지까지 불러온 상태다
    private var nextCursor: String? = null

    init {
        loadReports()
    }

    fun onTabSelected(tab: ReportHistoryTab) {
        _uiState.update { it.copy(selectedTab = tab) }
        loadMoreIfVisibleReportsShort()
    }

    fun onReportClicked(reportId: Long) {
        if (_uiState.value.isLoading) return

        suspend { reportRepository.getReportDetail(reportId) }
            .asFlow()
            .asResult()
            .onEach { result ->
                when (result) {
                    AppResult.Loading -> _uiState.update { it.copy(isLoading = true) }

                    is AppResult.Success -> _uiState.update {
                        it.copy(isLoading = false, selectedReport = result.data.toUiModel())
                    }

                    is AppResult.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _sideEffect.send(ReportHistorySideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    fun onReportDetailDismissed() {
        _uiState.update { it.copy(selectedReport = null) }
    }

    fun loadMore() {
        val current = _uiState.value
        if (current.isLoading || current.isLoadingMore) return
        val cursor = nextCursor ?: return

        suspend { reportRepository.getMyReports(cursor = cursor, limit = PAGE_SIZE) }
            .asFlow()
            .asResult()
            .onEach { result ->
                when (result) {
                    AppResult.Loading -> _uiState.update { it.copy(isLoadingMore = true) }

                    is AppResult.Success -> {
                        nextCursor = result.data.nextCursor
                        _uiState.update { state ->
                            state.copy(
                                isLoadingMore = false,
                                reports = state.reports + result.data.reports.map { it.toUiModel() },
                            )
                        }
                        loadMoreIfVisibleReportsShort()
                    }

                    is AppResult.Error -> {
                        _uiState.update { it.copy(isLoadingMore = false) }
                        _sideEffect.send(ReportHistorySideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadReports() {
        suspend { reportRepository.getMyReports(limit = PAGE_SIZE) }
            .asFlow()
            .asResult()
            .onEach { result ->
                when (result) {
                    AppResult.Loading -> _uiState.update { it.copy(isLoading = true) }

                    is AppResult.Success -> {
                        nextCursor = result.data.nextCursor
                        _uiState.update {
                            it.copy(isLoading = false, reports = result.data.reports.map { report -> report.toUiModel() })
                        }
                        loadMoreIfVisibleReportsShort()
                    }

                    is AppResult.Error -> {
                        _uiState.update { it.copy(isLoading = false) }
                        _sideEffect.send(ReportHistorySideEffect.ShowSnackbar(result.error.toMessageRes()))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    // 서버에 상태 필터가 없어 탭 필터를 클라이언트에서 한다. 고른 탭에 보이는 항목이 한 페이지보다 적으면
    // 스크롤이 생기지 않아 화면에서 다음 페이지를 요청할 수 없으므로, 채워질 때까지(또는 끝까지) 이어서 불러온다.
    private fun loadMoreIfVisibleReportsShort() {
        if (_uiState.value.visibleReports.size < PAGE_SIZE) loadMore()
    }

    private companion object {
        const val PAGE_SIZE = 20
    }
}
