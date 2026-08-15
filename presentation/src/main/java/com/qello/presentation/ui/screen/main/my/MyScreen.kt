package com.qello.presentation.ui.screen.main.my

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
    ) {
        Text("My Screen")
    }
}
