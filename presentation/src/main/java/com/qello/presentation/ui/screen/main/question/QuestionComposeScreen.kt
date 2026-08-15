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
fun QuestionComposeScreen(
    onNext: () -> Unit,
) {
    var isWriteSectionVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        Text("질문 선택 Section")
        Button(onClick = { isWriteSectionVisible = true }) { Text("이 질문 선택하기") }

        if (isWriteSectionVisible) {
            Text("질문 작성 Section (선택 후 아래에 등장)")
            Button(onClick = onNext) { Text("다음") }
        }
    }
}
