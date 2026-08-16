package com.qello.presentation.ui.screen.main.sent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val mockReplies = listOf(
    "저는 이런 사료 먹여요! 잘 먹더라고요",
    "수의사 상담도 같이 받아보세요~",
)

@Composable
fun SentQuestionDetailScreen(
    questionId: Int,
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("내가 보낸 질문 상세 (id=$questionId)")
        Box(modifier = Modifier.fillMaxWidth().size(200.dp).background(Color(0xFFE7A9A0)))
        Text("강아지를 새로 얻으려는데, 같이 지낼 사료 추천해요?", modifier = Modifier.padding(vertical = 12.dp))

        Text("받은 답변", modifier = Modifier.padding(top = 8.dp, bottom = 4.dp))
        LazyColumn {
            items(mockReplies) { reply ->
                Text(reply, modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}
