package com.qello.presentation.screen.main.received.list

import com.qello.domain.model.DirectionChip
import com.qello.presentation.model.InboxCardUiModel

data class ReceivedQuestionListUiState(
    val cards: List<InboxCardUiModel> = emptyList(),
    val chips: List<DirectionChip> = emptyList(),
    val selectedSegmentKey: String? = null,
    val answeredOnly: Boolean = false,
    val isLoading: Boolean = false,
)
