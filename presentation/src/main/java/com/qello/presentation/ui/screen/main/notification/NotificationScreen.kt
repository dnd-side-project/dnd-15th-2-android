package com.qello.presentation.ui.screen.main.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private enum class NotificationTab(val label: String) {
    ALL("전체"),
    RECEIVED("내게 온 질문"),
    SENT("내가 보낸 질문"),
}

@Composable
fun NotificationScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        SecondaryTabRow(selectedTabIndex = selectedTab) {
            NotificationTab.entries.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(tab.label) },
                )
            }
        }

        val items = when (NotificationTab.entries[selectedTab]) {
            NotificationTab.ALL -> listOf("북동쪽에서 질문이 도착했어요.", "내 질문에 새 답변이 달렸어요.", "내 질문에 공감을 눌렀어요.")
            NotificationTab.RECEIVED -> listOf("북동쪽에서 질문이 도착했어요.")
            NotificationTab.SENT -> listOf("내 질문에 새 답변이 달렸어요.")
        }

        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(items) { item ->
                Text(text = item, modifier = Modifier.padding(vertical = 12.dp))
            }
        }
    }
}
