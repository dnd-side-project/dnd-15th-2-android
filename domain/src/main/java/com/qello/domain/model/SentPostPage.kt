package com.qello.domain.model

data class SentPostPage(
    val cards: List<SentPostCard>,
    val nextCursor: SentPostCursor?,
)

data class SentPostCursor(
    val submittedAt: String,
    val postId: Long,
)
