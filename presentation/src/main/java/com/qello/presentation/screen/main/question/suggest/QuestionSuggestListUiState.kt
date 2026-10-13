package com.qello.presentation.screen.main.question.suggest

import com.qello.presentation.model.ProposalStatusUi
import com.qello.presentation.model.QuestionProposalUiModel

enum class QuestionSuggestListTab(val label: String) {
    ALL("전체"),
    REVIEWING("검토중"),
    COMPLETED("검토완료"),
}

data class QuestionSuggestListUiState(
    val proposals: List<QuestionProposalUiModel> = emptyList(),
    val selectedTab: QuestionSuggestListTab = QuestionSuggestListTab.ALL,
    val moreProposalId: Long? = null,
    val isLoading: Boolean = false,
) {
    val visibleProposals: List<QuestionProposalUiModel>
        get() = when (selectedTab) {
            QuestionSuggestListTab.ALL -> proposals
            QuestionSuggestListTab.REVIEWING -> proposals.filter { it.status == ProposalStatusUi.REVIEWING }
            QuestionSuggestListTab.COMPLETED -> proposals.filter { it.status != ProposalStatusUi.REVIEWING }
        }
}
