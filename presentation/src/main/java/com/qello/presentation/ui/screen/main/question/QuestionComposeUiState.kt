package com.qello.presentation.ui.screen.main.question

data class QuestionComposeUiState(
    val photoUri: String? = null,
    val content: String = "",
    val isUploadingPhoto: Boolean = false,
    val uploadedMediaId: Long? = null,
)
