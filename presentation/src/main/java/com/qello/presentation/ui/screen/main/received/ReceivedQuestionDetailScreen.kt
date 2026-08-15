package com.qello.presentation.ui.screen.main.received

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ReceivedQuestionDetailScreen(
    questionId: Int,
) {
    var replyInput by remember { mutableStateOf("") }
    val replies = remember { mutableStateListOf<String>() }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("내게 온 질문 상세 (id=$questionId)")
        Box(modifier = Modifier.fillMaxWidth().size(200.dp).background(Color(0xFFE7A9A0)))
        Text("강아지를 새로 얻으려는데, 강이지 이 아이 어떤가요?", modifier = Modifier.padding(vertical = 12.dp))

        LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
            items(replies) { reply ->
                Text(reply, modifier = Modifier.padding(vertical = 8.dp))
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = replyInput,
                onValueChange = { replyInput = it },
                label = { Text("답변 입력") },
            )
            Button(
                onClick = {
                    if (replyInput.isNotBlank()) {
                        replies.add(replyInput)
                        replyInput = ""
                    }
                },
            ) { Text("답변하기") }
        }
    }
}
