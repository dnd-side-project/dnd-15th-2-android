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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.qello.presentation.R
import com.qello.presentation.component.bottombar.HomeBottomBarTab
import com.qello.presentation.component.bottombar.QelloBottomBarScaffold
import com.qello.presentation.component.button.QelloIconButton
import com.qello.presentation.component.item.QelloQuestionCard
import com.qello.presentation.component.text.QelloText
import com.qello.presentation.ui.theme.QelloTheme

private data class SentQuestionItem(
    val id: Int,
    val question: String,
    val hasPhoto: Boolean,
    val location: String,
    val localTime: String,
    val commentCount: Int,
    val likeCount: Int,
    val postedAt: String,
    val distance: String,
    val newReplyCount: Int,
)

private val mockItems = listOf(
    SentQuestionItem(
        id = 1,
        question = "텍스트를 입력해주세요.",
        hasPhoto = true,
        location = "한국 서울시",
        localTime = "19:00",
        commentCount = 67,
        likeCount = 7,
        postedAt = "12:00:00",
        distance = "00km",
        newReplyCount = 0,
    ),
    SentQuestionItem(
        id = 2,
        question = "텍스트를 입력해주세요.",
        hasPhoto = false,
        location = "한국 서울시",
        localTime = "19:00",
        commentCount = 67,
        likeCount = 7,
        postedAt = "12:00:00",
        distance = "00km",
        newReplyCount = 0,
    ),
)

@Composable
fun SentQuestionListScreen(
    onItemClick: (Int) -> Unit,
    onNavigateToReceivedQuestion: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateHome: () -> Unit,
) {
    var answeredOnly by remember { mutableStateOf(false) }

    val filtered = mockItems.filter { !answeredOnly || it.newReplyCount > 0 }

    QelloBottomBarScaffold(
        selectedTab = HomeBottomBarTab.SENT,
        onTabClick = { tab ->
            if (tab == HomeBottomBarTab.RECEIVED) onNavigateToReceivedQuestion()
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
                    text = "보낸 질문",
                    style = QelloTheme.typography.heading2,
                    color = QelloTheme.colors.label.strong,
                    modifier = Modifier.weight(1f),
                )

                Row(horizontalArrangement = Arrangement.spacedBy(QelloTheme.spacing.spacing12)) {
                    QelloIconButton(
                        painter = painterResource(R.drawable.icon_bell),
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
                    ) { answeredOnly = !answeredOnly },
                ) {
                    QelloText(
                        text = "답변 온 질문만 보기",
                        style = QelloTheme.typography.caption2,
                        color = if (answeredOnly) QelloTheme.colors.label.strong else QelloTheme.colors.label.assistive,
                    )

                    Icon(
                        painter = painterResource(R.drawable.icon_check),
                        contentDescription = null,
                        tint = if (answeredOnly) QelloTheme.colors.label.strong else QelloTheme.colors.label.assistive,
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
                            painter = painterResource(R.drawable.icon_message),
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
                LazyColumn(
                    modifier = Modifier.padding(bottom = 69.dp),
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
                            badgeText = if (item.newReplyCount > 0) "새로운 답변 ${item.newReplyCount}개" else null,
                            onClick = { onItemClick(item.id) },
                            onMoreClick = {},
                        )
                    }
                }
            }
        }
    }
}
