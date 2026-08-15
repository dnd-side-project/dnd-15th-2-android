package com.qello.presentation.ui.screen.main.question

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuestionDirectionScreen(
    onSendComplete: () -> Unit,
) {
    var isSending by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        if (isSending) {
            Text("질문을 보내고 있어요! 잠시만 기다려주세요")
            Button(onClick = onSendComplete) { Text("(전송 완료 시뮬레이션)") }
        } else {
            Text("방향 선택 Screen")
            Button(onClick = { isSending = true }) { Text("이 방향으로 질문 보내기") }
        }
    }
}
