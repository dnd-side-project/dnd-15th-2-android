package com.qello.presentation.ui.screen.main.received

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.qello.presentation.R
import com.qello.presentation.component.bottombar.HomeBottomBarTab
import com.qello.presentation.component.bottombar.QelloBottomBarScaffold
import com.qello.presentation.component.button.QelloIconButton
import com.qello.presentation.component.item.QelloQuestionCard
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.designsystem.QelloColorPalette
import com.qello.presentation.ui.theme.QelloTheme

private enum class Direction(val label: String) {
    ALL("전체"),
    EAST("동쪽"),
    WEST("서쪽"),
}

private data class ReceivedQuestionItem(
    val id: Int,
    val question: String,
    val hasPhoto: Boolean,
    val location: String,
    val localTime: String,
    val commentCount: Int,
    val likeCount: Int,
    val postedAt: String,
    val distance: String,
    val isAnswered: Boolean,
    val direction: Direction,
)

private val mockItems = listOf(
    ReceivedQuestionItem(
        id = 1,
        question = "강아지를 새로 얻으려는데, 같이 지낼 사료 추천해주세요!",
        hasPhoto = true,
        location = "한국 서울시",
        localTime = "19:00",
        commentCount = 67,
        likeCount = 7,
        postedAt = "12:00:00",
        distance = "00km",
        isAnswered = false,
        direction = Direction.EAST,
    ),
    ReceivedQuestionItem(
        id = 2,
        question = "주식을 처음 시작하는데 어떤 걸 알아야 할까요?",
        hasPhoto = false,
        location = "한국 서울시",
        localTime = "19:00",
        commentCount = 67,
        likeCount = 7,
        postedAt = "12:00:00",
        distance = "00km",
        isAnswered = false,
        direction = Direction.WEST,
    ),
    ReceivedQuestionItem(
        id = 3,
        question = "강아지를 새로 얻으려는데, 산책은 하루에 몇 번이 좋을까요?",
        hasPhoto = true,
        location = "한국 서울시",
        localTime = "19:00",
        commentCount = 67,
        likeCount = 7,
        postedAt = "12:00:00",
        distance = "00km",
        isAnswered = true,
        direction = Direction.EAST,
    ),
)

@Composable
fun ReceivedQuestionListScreen(
    onItemClick: (Int) -> Unit,
    onNavigateToSentQuestion: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateHome: () -> Unit,
) {
    var selectedDirection by remember { mutableStateOf(Direction.ALL) }
    var answeredOnly by remember { mutableStateOf(false) }

    val filtered = mockItems
        .filter { selectedDirection == Direction.ALL || it.direction == selectedDirection }
        .filter { !answeredOnly || it.isAnswered }

    QelloBottomBarScaffold(
        selectedTab = HomeBottomBarTab.RECEIVED,
        onTabClick = { tab ->
            if (tab == HomeBottomBarTab.SENT) onNavigateToSentQuestion()
        },
        onCenterClick = onNavigateHome,
    ) {
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
                        painter = painterResource(R.drawable.icon_bell),
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

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = QelloTheme.spacing.spacing20)
                    .padding(top = QelloTheme.spacing.spacing12),
                horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing8),
            ) {
                Direction.entries.forEach { direction ->
                    DirectionChip(
                        label = direction.label,
                        selected = selectedDirection == direction,
                        onClick = { selectedDirection = direction },
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
                QelloText(
                    text = "내 위치 | 대구",
                    style = QelloTheme.typography.caption2,
                    color = QelloTheme.colors.label.assistive,
                    modifier = Modifier.weight(1f),
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing8),
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                    ) { answeredOnly = !answeredOnly },
                ) {
                    QelloText(
                        text = "답변한 글만 보기",
                        style = QelloTheme.typography.caption2,
                        color = QelloTheme.colors.label.assistive,
                    )

                    Box(
                        modifier = Modifier
                            .size(19.dp)
                            .clip(CircleShape)
                            .background(
                                if (answeredOnly) QelloTheme.colors.primary.normal else QelloTheme.colors.background.normalStrong,
                            ),
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
                        Box(
                            modifier = Modifier
                                .size(98.dp)
                                .clip(RoundedCornerShape(QelloTheme.radius.radius20))
                                .background(QelloTheme.colors.primary.normal),
                        )

                        Spacer(Modifier.height(QelloTheme.spacing.spacing20))

                        QelloText(
                            text = "아직 받은 질문이 없어요",
                            style = QelloTheme.typography.caption2,
                            color = QelloTheme.colors.label.assistive,
                            modifier = Modifier.padding(horizontal = QelloTheme.spacing.spacing20),
                        )

                        QelloText(
                            text = "다른 사람들에게 질문을 받아보세요!",
                            style = QelloTheme.typography.caption2.copy(textAlign = TextAlign.Center),
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
                    items(filtered) { item ->
                        QelloQuestionCard(
                            title = item.question,
                            hasPhoto = item.hasPhoto,
                            location = item.location,
                            localTime = item.localTime,
                            commentCount = item.commentCount,
                            likeCount = item.likeCount,
                            postedAt = item.postedAt,
                            distance = item.distance,
                            onClick = { onItemClick(item.id) },
                            onMoreClick = {},
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DirectionChip(
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

