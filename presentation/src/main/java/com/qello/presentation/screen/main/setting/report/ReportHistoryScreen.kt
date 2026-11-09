package com.qello.presentation.screen.main.setting.report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qello.presentation.R
import com.qello.presentation.designsystem.component.button.QelloBackButton
import com.qello.presentation.designsystem.component.item.QelloStatusItem
import com.qello.presentation.designsystem.component.loading.QelloLoadingOverlay
import com.qello.presentation.designsystem.component.tab.QelloTabRow
import com.qello.presentation.designsystem.component.text.QelloText
import com.qello.presentation.designsystem.theme.QelloTheme
import com.qello.presentation.model.ReportUiModel
import com.qello.presentation.screen.main.setting.report.component.ReportDetailDialog
import com.qello.presentation.screen.main.setting.report.component.dotColor
import com.qello.presentation.screen.main.setting.report.component.titleRes

// 마지막에서 이 개수 안쪽의 항목이 보이면 다음 페이지를 미리 불러온다
private const val LOAD_MORE_THRESHOLD = 3

@Composable
fun ReportHistoryRoute(
    onBack: () -> Unit,
    showSnackbar: suspend (message: String) -> Unit,
    viewModel: ReportHistoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val resources = LocalResources.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is ReportHistorySideEffect.ShowSnackbar -> showSnackbar(resources.getString(effect.message))
            }
        }
    }

    ReportHistoryScreen(
        uiState = uiState,
        onBack = onBack,
        onTabSelected = viewModel::onTabSelected,
        onReportClick = viewModel::onReportClicked,
        onReportDetailDismiss = viewModel::onReportDetailDismissed,
        onLoadMore = viewModel::loadMore,
    )
}

@Composable
fun ReportHistoryScreen(
    uiState: ReportHistoryUiState,
    onBack: () -> Unit,
    onTabSelected: (ReportHistoryTab) -> Unit,
    onReportClick: (reportId: Long) -> Unit,
    onReportDetailDismiss: () -> Unit,
    onLoadMore: () -> Unit,
) {
    val reports = uiState.visibleReports

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QelloTheme.colors.background.normalDefault),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = QelloTheme.spacing.spacing18,
                    top = QelloTheme.spacing.spacing36,
                    bottom = QelloTheme.spacing.spacing36,
                ),
        ) {
            QelloBackButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart))
            QelloText(
                text = stringResource(R.string.report_history_title),
                style = QelloTheme.typography.heading2,
                color = QelloTheme.colors.label.strong,
                modifier = Modifier.align(Alignment.Center),
            )
        }

        QelloTabRow(
            tabs = ReportHistoryTab.entries.map { stringResource(it.labelRes) },
            selectedIndex = uiState.selectedTab.ordinal,
            onTabSelected = { onTabSelected(ReportHistoryTab.entries[it]) },
        )

        when {
            reports.isNotEmpty() -> ReportList(
                reports = reports,
                isLoadingMore = uiState.isLoadingMore,
                onReportClick = onReportClick,
                onLoadMore = onLoadMore,
                modifier = Modifier.weight(1f),
            )

            // 고른 탭에 보일 항목을 찾으려고 다음 페이지를 이어서 불러오는 중
            uiState.isLoadingMore -> Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = QelloTheme.colors.label.assistive)
            }

            !uiState.isLoading -> ReportEmptyContent(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            )
        }
    }

    uiState.selectedReport?.let { report ->
        ReportDetailDialog(
            report = report,
            onDismiss = onReportDetailDismiss,
        )
    }

    if (uiState.isLoading) {
        QelloLoadingOverlay()
    }
}

@Composable
private fun ReportList(
    reports: List<ReportUiModel>,
    isLoadingMore: Boolean,
    onReportClick: (reportId: Long) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val groupedReports = remember(reports) { reports.groupBy { it.dateLabel } }
    val listState = rememberLazyListState()
    val currentOnLoadMore by rememberUpdatedState(onLoadMore)

    // 항목이 추가될 때마다 다시 시작해, 끝에 머문 채로 페이지가 붙은 경우에도 다시 판단하게 한다
    LaunchedEffect(listState, reports.size) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val lastVisibleIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@snapshotFlow false
            lastVisibleIndex >= layoutInfo.totalItemsCount - LOAD_MORE_THRESHOLD
        }.collect { isNearEnd ->
            if (isNearEnd) currentOnLoadMore()
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(
            start = QelloTheme.spacing.spacing20,
            end = QelloTheme.spacing.spacing20,
            top = QelloTheme.spacing.spacing24,
            bottom = QelloTheme.spacing.spacing16,
        ),
    ) {
        groupedReports.entries.forEachIndexed { groupIndex, (date, reportsForDate) ->
            item(key = "date_$date") {
                QelloText(
                    text = date,
                    style = QelloTheme.typography.caption2,
                    color = QelloTheme.colors.label.assistive,
                    modifier = Modifier.padding(top = if (groupIndex > 0) QelloTheme.spacing.spacing32 else 0.dp),
                )
            }

            items(reportsForDate, key = { it.id }) { report ->
                QelloStatusItem(
                    dotColor = report.status.dotColor(),
                    title = stringResource(report.status.titleRes()),
                    subtitle = stringResource(report.reasonLabelRes),
                    onClick = { onReportClick(report.id) },
                    modifier = Modifier.padding(top = 21.dp),
                )
            }
        }

        if (isLoadingMore) {
            item(key = "loading_more") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = QelloTheme.spacing.spacing16),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = QelloTheme.colors.label.assistive)
                }
            }
        }
    }
}

@Composable
private fun ReportEmptyContent(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(R.drawable.ic_message),
                contentDescription = null,
                tint = QelloTheme.colors.label.assistive,
                modifier = Modifier.size(width = 64.dp, height = 51.2.dp),
            )

            Spacer(Modifier.height(QelloTheme.spacing.spacing16))

            QelloText(
                text = stringResource(R.string.report_history_empty_title),
                style = QelloTheme.typography.caption2,
                color = QelloTheme.colors.label.assistive,
            )

            QelloText(
                text = stringResource(R.string.report_history_empty_caption),
                style = QelloTheme.typography.caption2,
                color = QelloTheme.colors.label.assistive,
            )
        }
    }
}
