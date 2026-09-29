package com.qello.presentation.ui.screen.main.sent

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qello.presentation.R
import com.qello.presentation.component.bottombar.HomeBottomBarTab
import com.qello.presentation.component.bottombar.QelloBottomBarScaffold
import com.qello.presentation.component.button.QelloIconButton
import com.qello.presentation.component.item.QelloQuestionCard
import com.qello.presentation.component.loading.QelloLoadingOverlay
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.designsystem.theme.QelloTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun SentQuestionListScreen(
    onItemClick: (Int) -> Unit,
    onNavigateToReceivedQuestion: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateHome: () -> Unit,
    showSnackbar: suspend (message: String) -> Unit,
    viewModel: SentQuestionListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val resources = LocalResources.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is SentQuestionListSideEffect.ShowSnackbar -> showSnackbar(resources.getString(effect.message))
            }
        }
    }

    val filtered = uiState.cards.filter { !uiState.answeredOnly || it.answerCount > 0 }

    QelloBottomBarScaffold(
        selectedTab = HomeBottomBarTab.SENT,
        onTabClick = { tab ->
            if (tab == HomeBottomBarTab.RECEIVED) onNavigateToReceivedQuestion()
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
                        text = "보낸 질문",
                        style = QelloTheme.typography.heading2,
                        color = QelloTheme.colors.label.strong,
                        modifier = Modifier.weight(1f),
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing12)) {
                        QelloIconButton(
                            painter = painterResource(R.drawable.ic_bell),
                            onClick = onNavigateToNotification,
                        )

                        // TODO: 메뉴(햄버거) 아이콘 에셋 추가되면 교체
                        QelloText(
                            text = "☰",
                            style = QelloTheme.typography.body1,
                            color = QelloTheme.colors.label.strong,
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(QelloTheme.colors.background.normalStrong),
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = QelloTheme.spacing.spacing20, vertical = QelloTheme.spacing.spacing12),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
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
                            text = "답변 온 질문만 보기",
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

                if (filtered.isEmpty()) {
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
                                text = "보낸 질문이 없어요",
                                style = QelloTheme.typography.caption2,
                                color = QelloTheme.colors.label.assistive,
                                modifier = Modifier.padding(horizontal = QelloTheme.spacing.spacing20),
                            )
                        }
                    }
                } else {
                    val listState = rememberLazyListState()

                    LaunchedEffect(listState, filtered.size) {
                        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                            .collect { lastVisibleIndex ->
                                if (lastVisibleIndex != null && lastVisibleIndex >= filtered.size - 3) {
                                    viewModel.loadMore()
                                }
                            }
                    }

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.padding(bottom = 69.dp),
                        contentPadding = PaddingValues(
                            start = QelloTheme.spacing.spacing20,
                            end = QelloTheme.spacing.spacing20,
                            top = QelloTheme.spacing.spacing16,
                            bottom = QelloTheme.spacing.spacing16,
                        ),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        items(filtered, key = { it.postId }) { card ->
                            QelloQuestionCard(
                                title = card.questionText,
                                hasPhoto = card.mediaIds.isNotEmpty(),
                                location = card.coarseRegionCode.orEmpty(),
                                localTime = card.submittedAt.toLocalTimeLabel(),
                                commentCount = card.answerCount.toInt(),
                                likeCount = card.reactionCount.toInt(),
                                postedAt = card.submittedAt.toPostedAtLabel(),
                                distance = "",
                                badgeText = if (card.unreadAnswerCount > 0) "새로운 답변 ${card.unreadAnswerCount}개" else null,
                                onClick = { onItemClick(card.postId.toInt()) },
                                onMoreClick = {},
                            )
                        }

                        if (uiState.isLoadingMore) {
                            item {
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
            }

            if (uiState.isLoading) {
                QelloLoadingOverlay()
            }
        }
    }
}

private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
private val timeWithSecondsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

private fun String.toLocalTimeLabel(): String =
    Instant.parse(this).atZone(ZoneId.systemDefault()).toLocalTime().format(timeFormatter)

private fun String.toPostedAtLabel(): String =
    Instant.parse(this).atZone(ZoneId.systemDefault()).toLocalTime().format(timeWithSecondsFormatter)
