package com.qello.domain.validation

object QuestionProposalValidator {
    const val MAX_LENGTH = 2000

    fun validate(proposedText: String): QuestionProposalError? {
        val trimmed = proposedText.trim()
        return when {
            trimmed.isEmpty() -> QuestionProposalError.BLANK
            trimmed.length > MAX_LENGTH -> QuestionProposalError.OUT_OF_LENGTH
            else -> null
        }
    }
}
