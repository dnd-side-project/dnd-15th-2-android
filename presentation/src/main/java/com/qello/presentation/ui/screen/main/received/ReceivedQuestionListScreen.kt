package com.qello.presentation.ui.screen.main.received

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qello.domain.model.InboxCard
import com.qello.presentation.R
import com.qello.presentation.component.bottombar.HomeBottomBarTab
import com.qello.presentation.component.bottombar.QelloBottomBarScaffold
import com.qello.presentation.component.bottomsheet.QelloMoreBottomSheet
import com.qello.presentation.component.bottomsheet.QelloReportBottomSheet
import com.qello.presentation.component.bottomsheet.QelloReportCompleteOverlay
import com.qello.presentation.component.button.QelloIconButton
import com.qello.presentation.component.item.QelloQuestionCard
import com.qello.presentation.component.loading.QelloLoadingOverlay
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.designsystem.QelloColorPalette
import com.qello.presentation.ui.designsystem.theme.QelloTheme
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun ReceivedQuestionListScreen(
    onItemClick: (Int) -> Unit,
    onNavigateToSentQuestion: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateHome: () -> Unit,
    showSnackbar: suspend (message: String) -> Unit,
    viewModel: ReceivedQuestionListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val resources = LocalResources.current
    val coroutineScope = rememberCoroutineScope()
    val hideSnackbarHostState = remember { SnackbarHostState() }

    var moreCard by remember { mutableStateOf<InboxCard?>(null) }
    var reportTargetId by remember { mutableStateOf<Long?>(null) }
    var showReportComplete by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is ReceivedQuestionListSideEffect.ShowSnackbar -> showSnackbar(resources.getString(effect.message))
                ReceivedQuestionListSideEffect.ReportSubmitted -> showReportComplete = true

                is ReceivedQuestionListSideEffect.PostHidden -> {
                    coroutineScope.launch {
                        val result = hideSnackbarHostState.showSnackbar(
                            message = "글을 숨겼어요",
                            actionLabel = "실행취소",
                            duration = SnackbarDuration.Short,
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onHideUndo(effect.postRecipientId)
                        }
                    }
                }

                ReceivedQuestionListSideEffect.HideUndone -> {
                    hideSnackbarHostState.showSnackbar(message = "실행취소했어요", duration = SnackbarDuration.Short)
                }
            }
        }
    }

    QelloBottomBarScaffold(
        selectedTab = HomeBottomBarTab.RECEIVED,
        onTabClick = { tab ->
            if (tab == HomeBottomBarTab.SENT) onNavigateToSentQuestion()
        },
        onCenterClick = onNavigateHome,
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(QelloTheme.gradient.backgroundDefault),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = QelloTheme.spacing.spacing20)
                        .padding(top = QelloTheme.spacing.spacing24, bottom = QelloTheme.spacing.spacing8),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    QelloText(
                        text = stringResource(R.string.bottom_bar_received_label),
                        style = QelloTheme.typography.heading2,
                        color = QelloTheme.colors.label.strong,
                        modifier = Modifier.weight(1f),
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing12)) {
                        QelloIconButton(
                            painter = painterResource(R.drawable.ic_bell),
                            onClick = onNavigateToNotification,
                        )

                        Box(
                            modifier = Modifier
                                .size(QelloTheme.iconSize.size24)
                                .clip(RoundedCornerShape(QelloTheme.radius.radius8))
                                .background(QelloTheme.colors.primary.normal),
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(QelloTheme.colors.background.normalStrong),
                )

                if (uiState.chips.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = QelloTheme.spacing.spacing20)
                            .padding(top = QelloTheme.spacing.spacing12),
                        horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing8),
                    ) {
                        DirectionChipButton(
                            label = "전체",
                            selected = uiState.selectedSegmentKey == null,
                            onClick = { viewModel.onSegmentSelected(null) },
                        )

                        uiState.chips.sortedBy { it.sortOrder }.forEach { chip ->
                            DirectionChipButton(
                                label = chip.displayName,
                                selected = uiState.selectedSegmentKey == chip.segmentKey,
                                onClick = { viewModel.onSegmentSelected(chip.segmentKey) },
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = QelloTheme.spacing.spacing20)
                            .padding(top = QelloTheme.spacing.spacing20, bottom = QelloTheme.spacing.spacing12),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Spacer(Modifier.weight(1f))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing8),
                            modifier = Modifier.clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = viewModel::onAnsweredOnlyToggled,
                            ),
                        ) {
                            QelloText(
                                text = "답변한 글만 보기",
                                style = QelloTheme.typography.caption2,
                                color = if (uiState.answeredOnly) QelloTheme.colors.label.strong else QelloTheme.colors.label.assistive,
                            )

                            Icon(
                                painter = painterResource(R.drawable.ic_check),
                                contentDescription = null,
                                tint = if (uiState.answeredOnly) QelloTheme.colors.label.strong else QelloTheme.colors.label.assistive,
                                modifier = Modifier.size(width = 18.dp, height = 12.dp),
                            )
                        }
                    }
                }

                if (uiState.cards.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(bottom = 102.dp + 37.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                painter = painterResource(R.drawable.ic_message),
                                contentDescription = null,
                                tint = QelloTheme.colors.label.assistive,
                                modifier = Modifier.size(width = 65.dp, height = 51.2.dp),
                            )

                            Spacer(Modifier.height(QelloTheme.spacing.spacing20))

                            QelloText(
                                text = "받은 질문이 없어요!",
                                style = QelloTheme.typography.caption2,
                                color = QelloTheme.colors.label.assistive,
                                modifier = Modifier.padding(horizontal = QelloTheme.spacing.spacing20),
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.padding(bottom = 63.dp),
                        contentPadding = PaddingValues(
                            start = QelloTheme.spacing.spacing20,
                            end = QelloTheme.spacing.spacing20,
                            top = QelloTheme.spacing.spacing16,
                            bottom = QelloTheme.spacing.spacing16,
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        items(uiState.cards, key = { it.postRecipientId }) { card ->
                            QelloQuestionCard(
                                title = card.questionText,
                                hasPhoto = card.mediaIds.isNotEmpty(),
                                location = card.senderCoarseRegionCode.orEmpty(),
                                localTime = card.matchedAt.toLocalTimeLabel(),
                                commentCount = card.answerCount.toInt(),
                                likeCount = card.reactionCount.toInt(),
                                postedAt = card.matchedAt.toPostedAtLabel(),
                                distance = card.toDistanceLabel(),
                                onClick = { onItemClick(card.postRecipientId.toInt()) },
                                onMoreClick = { moreCard = card },
                            )
                        }
                    }
                }
            }

            if (uiState.isLoading) {
                QelloLoadingOverlay()
            }

            if (showReportComplete) {
                QelloReportCompleteOverlay(onCloseClick = { showReportComplete = false })
            }

            SnackbarHost(
                hostState = hideSnackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 102.dp + 37.dp),
            )
        }
    }

    if (moreCard != null) {
        QelloMoreBottomSheet(
            onDismissRequest = { moreCard = null },
            onHideClick = {
                viewModel.onHideSubmit(moreCard!!.postRecipientId)
                moreCard = null
            },
            onReportClick = {
                reportTargetId = moreCard!!.postId
                moreCard = null
            },
        )
    }

    if (reportTargetId != null) {
        QelloReportBottomSheet(
            onDismissRequest = { reportTargetId = null },
            onReasonSelected = { reasonCode ->
                viewModel.onReportSubmit(reportTargetId!!, reasonCode)
                reportTargetId = null
            },
        )
    }
}

@Composable
private fun DirectionChipButton(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .background(if (selected) QelloTheme.colors.primary.normal else QelloColorPalette.Navy20)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            )
            .padding(horizontal = QelloTheme.spacing.spacing16, vertical = QelloTheme.spacing.spacing8),
    ) {
        QelloText(
            text = label,
            style = QelloTheme.typography.caption1,
            color = if (selected) QelloTheme.colors.label.strong else QelloColorPalette.Navy60,
        )
    }
}

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val timeWithSecondsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

private fun String.toLocalTimeLabel(): String =
    Instant.parse(this).atZone(ZoneId.systemDefault()).toLocalTime().format(timeFormatter)

private fun String.toPostedAtLabel(): String =
    Instant.parse(this).atZone(ZoneId.systemDefault()).toLocalTime().format(timeWithSecondsFormatter)

private fun InboxCard.toDistanceLabel(): String {
    distanceBand?.let { return it }
    val meters = distanceM ?: return ""
    return if (meters >= 1000) "${meters / 1000}km" else "${meters}m"
}
