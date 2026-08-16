package com.qello.presentation.ui.screen.main.sent

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

private data class SentQuestionItem(
    val id: Int,
    val question: String,
    val isAnswered: Boolean,
)

private val mockItems = listOf(
    SentQuestionItem(1, "강아지를 새로 얻으려는데, 같이 지낼 사료 추천해요?", isAnswered = true),
    SentQuestionItem(2, "강아지를 새로 얻으려는데, 같이 지낼 곳도 마련해야하나요?", isAnswered = false),
)

@Composable
fun SentQuestionListScreen(
    onItemClick: (Int) -> Unit,
) {
    var unansweredOnly by remember { mutableStateOf(false) }

    val filtered = mockItems.filter { !unansweredOnly || !it.isAnswered }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("내가 보낸 질문", modifier = Modifier.padding(16.dp))

        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Checkbox(checked = unansweredOnly, onCheckedChange = { unansweredOnly = it })
            Text("답변 없는 질문만 보기")
        }

        if (filtered.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text("보낸 질문이 없어요")
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
                        Text(if (item.isAnswered) "답변 있음" else "답변 없음")
                    }
                }
            }
        }
    }
}
