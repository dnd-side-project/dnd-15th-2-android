package com.qello.presentation.ui.screen.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    onNavigateToQuestionCompose: () -> Unit,
    onNavigateToNotification: () -> Unit,
    onNavigateToReceivedQuestion: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Main Screen")
        Button(onClick = onNavigateToQuestionCompose) { Text("질문 보내기") }
        Button(onClick = onNavigateToNotification) { Text("알림") }
        Button(onClick = onNavigateToReceivedQuestion) { Text("내게 온 질문") }
    }
}
