package com.qello.presentation.ui.screen.main.question

import com.qello.domain.model.RecommendedQuestion

data class QuestionComposeUiState(
    val questions: List<RecommendedQuestion> = emptyList(),
    val isLoadingQuestions: Boolean = false,
    val photoUri: String? = null,
    val content: String = "",
    val isUploadingPhoto: Boolean = false,
    val uploadedMediaId: Long? = null,
)
