package com.qello.presentation.ui.screen.main.notification

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.qello.presentation.component.QelloBackButton
import com.qello.presentation.component.QelloNotificationItem
import com.qello.presentation.component.QelloTabRow
import com.qello.presentation.component.QelloText
import com.qello.presentation.ui.theme.QelloTheme

private enum class NotificationTab(val label: String) {
    ALL("전체"),
    RECEIVED("내게 온 질문"),
    SENT("내가 보낸 질문"),
}

private data class NotificationUiModel(
    val title: String,
    val description: String,
    val timeAgo: String,
    val isUnread: Boolean,
    val category: NotificationTab,
)

@Composable
fun NotificationScreen(
    onBack: () -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }

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
                text = "알림",
                style = QelloTheme.typography.heading2,
                color = QelloTheme.colors.label.strong,
                modifier = Modifier.align(Alignment.Center),
            )
        }

        QelloTabRow(
            tabs = NotificationTab.entries.map { it.label },
            selectedIndex = selectedTab,
            onTabSelected = { selectedTab = it },
        )

        val allItems = remember {
            listOf(
                NotificationUiModel(
                    title = "내 질문에 새 답변이 달렸어요.",
                    description = "보낸 질문에 답변이 도착했어요. 어떤 이야기인지 확인해보세요.",
                    timeAgo = "3시간 전",
                    isUnread = true,
                    category = NotificationTab.SENT,
                ),
                NotificationUiModel(
                    title = "내 질문에 공감을 눌렀어요.",
                    description = "다른 사용자가 내 질문에 공감을 눌렀어요.",
                    timeAgo = "1일 전",
                    isUnread = false,
                    category = NotificationTab.SENT,
                ),
            )
        }

        val items = when (val tab = NotificationTab.entries[selectedTab]) {
            NotificationTab.ALL -> allItems
            else -> allItems.filter { it.category == tab }
        }

        if (items.isEmpty()) {
            val (emptyTitle, emptyDescription) = when (NotificationTab.entries[selectedTab]) {
                NotificationTab.ALL -> "아직 도착한 알림이 없어요" to "새로운 소식이 오면 바로 알려드릴게요."
                NotificationTab.RECEIVED -> "아직 받은 질문이 없어요" to "다른 사람들에게 질문을 받아보세요!"
                NotificationTab.SENT -> "아직 보낸 질문이 없어요" to "누군가에게 질문을 보내보세요!"
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(QelloTheme.colors.primary.normal),
                    )

                    Spacer(Modifier.height(QelloTheme.spacing.spacing20))

                    QelloText(
                        text = emptyTitle,
                        style = QelloTheme.typography.caption2,
                        color = QelloTheme.colors.label.assistive,
                        modifier = Modifier.padding(horizontal = QelloTheme.spacing.spacing20),
                    )

                    QelloText(
                        text = emptyDescription,
                        style = QelloTheme.typography.caption2.copy(textAlign = TextAlign.Center),
                        color = QelloTheme.colors.label.assistive,
                        modifier = Modifier.padding(horizontal = QelloTheme.spacing.spacing20),
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(
                    top = QelloTheme.spacing.spacing16,
                    bottom = QelloTheme.spacing.spacing16,
                ),
            ) {
                items(items) { item ->
                    QelloNotificationItem(
                        title = item.title,
                        description = item.description,
                        timeAgo = item.timeAgo,
                        isUnread = item.isUnread,
                        onClick = {},
                        onMoreClick = {},
                    )
                }
            }
        }
    }
}
