package com.qello.domain.repository

import com.qello.domain.model.Answer
import com.qello.domain.model.PostReaction

interface DirectionRepository {
    suspend fun updatePresence(
        latitude: Double,
        longitude: Double,
        accuracyMeters: Double,
        receiveAllowed: Boolean = true,
    )

    /** 고른 방향에 있는 사람들에게 질문글을 보낸다. */
    suspend fun submitDirectionPost(
        approvedQuestionId: Long,
        schemeId: Long,
        segmentKey: String,
        bodyText: String?,
        mediaIds: List<Long>,
        idempotencyKey: String,
    )

    /** 수신 자격이 있는 사용자가 질문글에 공감한다. 이미 공감한 상태면 그대로 돌려준다. */
    suspend fun reactToPost(postId: Long): PostReaction

    /** 질문글 공감을 취소한다. 공감이 없는 상태에서 불러도 실패하지 않는다. */
    suspend fun cancelPostReaction(postId: Long): PostReaction

    /** 질문글에 달린 답변을 최신순으로 가져온다. 자격이 없으면 빈 목록이 온다. */
    suspend fun getAnswers(
        postId: Long,
        limit: Int = 20,
    ): List<Answer>

    /** 질문글 작성자이거나 그 질문글의 수신 자격자가 답변에 공감한다. 자기 답변에는 공감할 수 없다. */
    suspend fun reactToAnswer(answerId: Long): PostReaction

    /** 답변 공감을 취소한다. 공감이 없는 상태에서 불러도 실패하지 않는다. */
    suspend fun cancelAnswerReaction(answerId: Long): PostReaction
}
