package com.qello.presentation.ui.screen.main.received

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.qello.presentation.component.bottombar.HomeBottomBarTab
import com.qello.presentation.component.bottombar.QelloBottomBarScaffold

private enum class Direction(val label: String) {
    ALL("전체"),
    EAST("동쪽"),
    WEST("서쪽"),
}

private data class ReceivedQuestionItem(
    val id: Int,
    val question: String,
    val isAnswered: Boolean,
    val direction: Direction,
)

private val mockItems = listOf(
    ReceivedQuestionItem(1, "강아지를 새얻으려는데, 강이 저 좋아요?", isAnswered = false, direction = Direction.EAST),
    ReceivedQuestionItem(2, "주섯을 좋아하는데 이런 뭐 알려주세요", isAnswered = true, direction = Direction.WEST),
    ReceivedQuestionItem(3, "강아지를 새얻으려는데, 강이 저 좋아요?", isAnswered = true, direction = Direction.EAST),
)

@Composable
fun ReceivedQuestionListScreen(
    onItemClick: (Int) -> Unit,
    onNavigateToSentQuestion: () -> Unit,
    onNavigateHome: () -> Unit,
) {
    var selectedDirection by remember { mutableStateOf(Direction.ALL) }
    var unansweredOnly by remember { mutableStateOf(false) }

    val filtered = mockItems
        .filter { selectedDirection == Direction.ALL || it.direction == selectedDirection }
        .filter { !unansweredOnly || !it.isAnswered }

    QelloBottomBarScaffold(
        selectedTab = HomeBottomBarTab.RECEIVED,
        onTabClick = { tab ->
            if (tab == HomeBottomBarTab.SENT) onNavigateToSentQuestion()
        },
        onCenterClick = onNavigateHome,
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text("내게 온 질문", modifier = Modifier.padding(16.dp))

            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                Direction.entries.forEach { direction ->
                    FilterChip(
                        selected = selectedDirection == direction,
                        onClick = { selectedDirection = direction },
                        label = { Text(direction.label) },
                        modifier = Modifier.padding(end = 8.dp),
                    )
                }
            }

            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(checked = unansweredOnly, onCheckedChange = { unansweredOnly = it })
                Text("답변하지 않은 글만 보기")
            }

            if (filtered.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("아직 받은 질문이 없어요")
                }
            } else {
                LazyColumn(modifier = Modifier.padding(16.dp)) {
                    items(filtered) { item ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .clickable { onItemClick(item.id) },
                        ) {
                            Box(modifier = Modifier.size(56.dp).background(Color(0xFFE7A9A0)))
                            Text(item.question, modifier = Modifier.padding(top = 8.dp))
                            Text(if (item.isAnswered) "답변 완료" else "답변 안함")
                        }
                    }
                }
            }
        }
    }
}
