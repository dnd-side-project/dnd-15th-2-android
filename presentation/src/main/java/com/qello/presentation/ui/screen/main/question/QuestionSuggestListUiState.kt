package com.qello.presentation.ui.screen.main.question

import com.qello.domain.model.QuestionProposal
import com.qello.domain.model.QuestionProposalStatus

enum class QuestionSuggestListTab(val label: String) {
    ALL("전체"),
    REVIEWING("검토중"),
    COMPLETED("검토완료"),
}

data class QuestionSuggestListUiState(
    val proposals: List<QuestionProposal> = emptyList(),
    val selectedTab: QuestionSuggestListTab = QuestionSuggestListTab.ALL,
    val moreProposalId: Long? = null,
    val isLoading: Boolean = false,
) {
    val visibleProposals: List<QuestionProposal>
        get() = when (selectedTab) {
            QuestionSuggestListTab.ALL -> proposals
            QuestionSuggestListTab.REVIEWING -> proposals.filter { it.status.isReviewing() }
            QuestionSuggestListTab.COMPLETED -> proposals.filter { !it.status.isReviewing() }
        }
}

private fun QuestionProposalStatus.isReviewing(): Boolean = when (this) {
    QuestionProposalStatus.DRAFT, QuestionProposalStatus.SUBMITTED, QuestionProposalStatus.UNDER_REVIEW -> true
    QuestionProposalStatus.APPROVED, QuestionProposalStatus.REJECTED, QuestionProposalStatus.ARCHIVED -> false
}
