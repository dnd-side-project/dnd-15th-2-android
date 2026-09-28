package com.qello.presentation.ui.screen.main.received

import com.qello.domain.model.DirectionChip
import com.qello.domain.model.InboxCard

data class ReceivedQuestionListUiState(
    val cards: List<InboxCard> = emptyList(),
    val chips: List<DirectionChip> = emptyList(),
    val selectedSegmentKey: String? = null,
    val answeredOnly: Boolean = false,
    val isLoading: Boolean = false,
)
