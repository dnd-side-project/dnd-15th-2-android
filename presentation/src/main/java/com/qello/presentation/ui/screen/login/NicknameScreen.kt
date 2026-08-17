package com.qello.presentation.ui.screen.login

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
import com.qello.presentation.component.QelloTextField

@Composable
fun NicknameScreen(
    onNavigateToMain: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
    ) {
        Text("Nickname Screen")

        var nickname by remember { mutableStateOf("") }

        QelloTextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = "닉네임",
        )

        QelloTextField(
            value = nickname,
            onValueChange = { nickname = it },
            label = "닉네임",
            isError = true,
            supportingText = "에러 메세지!!",
        )

        Button(onClick = onNavigateToMain) { Text("메인으로 이동 (로그인 완료)") }
    }
}
