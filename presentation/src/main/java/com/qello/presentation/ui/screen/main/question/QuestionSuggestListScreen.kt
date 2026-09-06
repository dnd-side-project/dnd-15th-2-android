package com.qello.presentation.ui.screen.main.question

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.qello.presentation.R
import com.qello.presentation.component.button.QelloBackButton
import com.qello.presentation.component.button.QelloLargeButton
import com.qello.presentation.component.button.QelloSmallButton
import com.qello.presentation.component.item.QelloActionSheetItem
import com.qello.presentation.component.item.QelloSuggestQuestionStatusItem
import com.qello.presentation.component.tab.QelloTabRow
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.theme.QelloTheme

private enum class SuggestQuestionStatus {
    REVIEWING,
    APPROVED,
    REJECTED,
}

private enum class QuestionSuggestListTab(val label: String) {
    ALL("전체"),
    REVIEWING("검토중"),
    COMPLETED("검토완료"),
}

private data class SuggestQuestionUiModel(
    val date: String,
    val status: SuggestQuestionStatus,
)

// TODO: 빈 화면 확인을 위해 검토중(REVIEWING) 목데이터를 임시로 비워둠
private val mockItems = listOf(
    SuggestQuestionUiModel(date = "2026.07.02", status = SuggestQuestionStatus.APPROVED),
    SuggestQuestionUiModel(date = "2026.07.02", status = SuggestQuestionStatus.REJECTED),
    SuggestQuestionUiModel(date = "2026.07.01", status = SuggestQuestionStatus.APPROVED),
    SuggestQuestionUiModel(date = "2026.07.01", status = SuggestQuestionStatus.REJECTED),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionSuggestListScreen(
    onBack: () -> Unit,
    onNavigateToSuggestCompose: () -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showMoreSheet by remember { mutableStateOf(false) }

    val items = when (QuestionSuggestListTab.entries[selectedTab]) {
        QuestionSuggestListTab.ALL -> mockItems
        QuestionSuggestListTab.REVIEWING -> mockItems.filter { it.status == SuggestQuestionStatus.REVIEWING }
        QuestionSuggestListTab.COMPLETED -> mockItems.filter { it.status != SuggestQuestionStatus.REVIEWING }
    }

    val groupedItems = items.groupBy { it.date }

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

        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(R.drawable.icon_message),
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
                groupedItems.entries.forEachIndexed { groupIndex, (date, itemsForDate) ->
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

                    itemsForDate.forEach { suggestQuestion ->
                        item { Spacer(Modifier.height(21.dp)) }

                        item {
                            val (dotColor, title) = when (suggestQuestion.status) {
                                SuggestQuestionStatus.REVIEWING -> QelloTheme.colors.primary.normal to "현재 검토중이예요!"
                                SuggestQuestionStatus.APPROVED -> QelloTheme.colors.status.positive to "질문이 승인됐어요! 카드로 질문을 만나보세요!"
                                SuggestQuestionStatus.REJECTED -> QelloTheme.colors.status.destructive to "질문이 거절당했어요."
                            }

                            QelloSuggestQuestionStatusItem(
                                dotColor = dotColor,
                                title = title,
                                subtitle = "사용자가 제안한 질문",
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
}
