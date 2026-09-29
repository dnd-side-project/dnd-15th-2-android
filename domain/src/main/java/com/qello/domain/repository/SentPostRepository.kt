package com.qello.domain.repository

import com.qello.domain.model.SentPostCursor
import com.qello.domain.model.SentPostDetail
import com.qello.domain.model.SentPostFilter
import com.qello.domain.model.SentPostPage

interface SentPostRepository {
    /** cursor를 생략하면 첫 페이지를, 이전 페이지의 nextCursor를 넘기면 다음 페이지를 가져온다. */
    suspend fun getSentPosts(
        filter: SentPostFilter = SentPostFilter.ALL,
        cursor: SentPostCursor? = null,
        limit: Int = 20,
    ): SentPostPage

    /** 내가 보낸 질문글 하나를 조회한다. 남의 질문글이거나 없으면 실패한다. */
    suspend fun getSentPostDetail(postId: Long): SentPostDetail
}
