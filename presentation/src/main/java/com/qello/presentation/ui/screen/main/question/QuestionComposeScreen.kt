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
import com.qello.presentation.component.text.QelloTextArea

@Composable
fun QuestionComposeScreen(
    onNext: () -> Unit,
    onNavigateToSuggest: () -> Unit,
) {
    var isWriteSectionVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        Text("질문 선택 Section")
        Button(onClick = { isWriteSectionVisible = true }) { Text("이 질문 선택하기") }
        Button(onClick = onNavigateToSuggest) { Text("질문 제안하러가기") }

        var content by remember { mutableStateOf("") }

        if (isWriteSectionVisible) {
            QelloTextArea(
                value = content,
                onValueChange = { content = it },
            )

            QelloTextArea(
                value = content,
                onValueChange = { content = it },
                isError = true,
                supportingText = "에러 메세지!!",
            )

            Button(onClick = onNext) { Text("다음") }
        }
    }
}
