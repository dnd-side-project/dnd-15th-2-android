package com.qello.presentation.ui.screen.main.question

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.qello.domain.model.QuestionProposalStatus
import com.qello.presentation.R
import com.qello.presentation.component.button.QelloBackButton
import com.qello.presentation.component.button.QelloLargeButton
import com.qello.presentation.component.button.QelloSmallButton
import com.qello.presentation.component.item.QelloActionSheetItem
import com.qello.presentation.component.item.QelloSuggestQuestionStatusItem
import com.qello.presentation.component.loading.QelloLoadingOverlay
import com.qello.presentation.component.tab.QelloTabRow
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.designsystem.theme.QelloTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private enum class QuestionSuggestListTab(val label: String) {
    ALL("전체"),
    REVIEWING("검토중"),
    COMPLETED("검토완료"),
}

private val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionSuggestListScreen(
    onBack: () -> Unit,
    onNavigateToSuggestCompose: () -> Unit,
    showSnackbar: suspend (message: String) -> Unit,
    viewModel: QuestionSuggestListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val resources = LocalResources.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is QuestionSuggestListSideEffect.ShowSnackbar -> showSnackbar(resources.getString(effect.message))
            }
        }
    }

    var selectedTab by remember { mutableIntStateOf(0) }
    var showMoreSheet by remember { mutableStateOf(false) }

    val proposals = when (QuestionSuggestListTab.entries[selectedTab]) {
        QuestionSuggestListTab.ALL -> uiState.proposals
        QuestionSuggestListTab.REVIEWING -> uiState.proposals.filter { it.status.isReviewing() }
        QuestionSuggestListTab.COMPLETED -> uiState.proposals.filter { !it.status.isReviewing() }
    }

    val groupedProposals = proposals.groupBy { it.createdAt.toDateLabel() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(QelloTheme.colors.background.normalDefault),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 18.dp, top = 36.dp, bottom = 36.dp),
        ) {
            QelloBackButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart))
            QelloText(
                text = "제안한 질문",
                style = QelloTheme.typography.heading2,
                color = QelloTheme.colors.label.strong,
                modifier = Modifier.align(Alignment.Center),
            )
        }

        QelloTabRow(
            tabs = QuestionSuggestListTab.entries.map { it.label },
            selectedIndex = selectedTab,
            onTabSelected = { selectedTab = it },
        )

        if (proposals.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
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
                        text = "제안한 질문이 없어요.",
                        style = QelloTheme.typography.caption2,
                        color = QelloTheme.colors.label.assistive,
                    )

                    QelloText(
                        text = "제안하기를 눌러 질문을 제안해보세요!",
                        style = QelloTheme.typography.caption2,
                        color = QelloTheme.colors.label.assistive,
                    )

                    Spacer(Modifier.height(30.dp))

                    QelloSmallButton(
                        text = stringResource(R.string.question_suggest_button),
                        colors = QelloTheme.buttonColors.darkButtonColors,
                        onClick = onNavigateToSuggestCompose,
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    start = QelloTheme.spacing.spacing20,
                    end = QelloTheme.spacing.spacing20,
                    top = QelloTheme.spacing.spacing24,
                    bottom = QelloTheme.spacing.spacing16,
                ),
            ) {
                groupedProposals.entries.forEachIndexed { groupIndex, (date, proposalsForDate) ->
                    if (groupIndex > 0) {
                        item { Spacer(Modifier.height(32.dp)) }
                    }

                    item {
                        QelloText(
                            text = date,
                            style = QelloTheme.typography.caption2,
                            color = QelloTheme.colors.label.assistive,
                        )
                    }

                    proposalsForDate.forEach { proposal ->
                        item { Spacer(Modifier.height(21.dp)) }

                        item {
                            val (dotColor, title) = when (proposal.status) {
                                QuestionProposalStatus.DRAFT,
                                QuestionProposalStatus.SUBMITTED,
                                QuestionProposalStatus.UNDER_REVIEW,
                                -> QelloTheme.colors.primary.normal to "현재 검토중이예요!"

                                QuestionProposalStatus.APPROVED ->
                                    QelloTheme.colors.status.positive to "질문이 승인됐어요! 카드로 질문을 만나보세요!"

                                QuestionProposalStatus.REJECTED -> QelloTheme.colors.status.destructive to "질문이 거절당했어요."

                                QuestionProposalStatus.ARCHIVED -> QelloTheme.colors.label.assistive to "보관된 제안이에요."
                            }

                            QelloSuggestQuestionStatusItem(
                                dotColor = dotColor,
                                title = title,
                                subtitle = proposal.proposedText,
                                onMoreClick = { showMoreSheet = true },
                            )
                        }
                    }
                }
            }
        }

        if (showMoreSheet) {
            ModalBottomSheet(
                onDismissRequest = { showMoreSheet = false },
                containerColor = Color.Transparent,
                shape = RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            QelloTheme.gradient.backgroundStrong,
                            RoundedCornerShape(topStart = 50.dp, topEnd = 50.dp),
                        )
                        .padding(top = 40.dp, bottom = QelloTheme.spacing.spacing20),
                ) {
                    QelloActionSheetItem(
                        text = "알림 받지 않기",
                        onClick = { showMoreSheet = false },
                        modifier = Modifier.padding(horizontal = 30.dp),
                    )

                    Spacer(Modifier.height(QelloTheme.spacing.spacing12))

                    QelloActionSheetItem(
                        text = "삭제하기",
                        onClick = { showMoreSheet = false },
                        modifier = Modifier.padding(horizontal = 30.dp),
                    )

                    Spacer(Modifier.height(50.dp))

                    QelloLargeButton(
                        text = "닫기",
                        colors = QelloTheme.buttonColors.darkButtonColors,
                        onClick = { showMoreSheet = false },
                        modifier = Modifier.padding(horizontal = 18.dp),
                    )
                }
            }
        }
    }

    if (uiState.isLoading) {
        QelloLoadingOverlay()
    }
}

private fun QuestionProposalStatus.isReviewing(): Boolean = when (this) {
    QuestionProposalStatus.DRAFT, QuestionProposalStatus.SUBMITTED, QuestionProposalStatus.UNDER_REVIEW -> true
    QuestionProposalStatus.APPROVED, QuestionProposalStatus.REJECTED, QuestionProposalStatus.ARCHIVED -> false
}

private fun String.toDateLabel(): String =
    Instant.parse(this).atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter)
