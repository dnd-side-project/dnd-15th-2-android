package com.qello.presentation.screen.main.setting.info

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.qello.presentation.designsystem.component.button.QelloBackButton

@Composable
fun AppInfoScreen(
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        QelloBackButton(onClick = onBack)

        Text(text = "App Info Screen")
    }
}
