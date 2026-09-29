package com.qello.domain.model

/** `내게 온 질문` 화면의 두 카테고리. 둘 다 만료 전 항목만 담는다. */
enum class InboxCategory {
    /** 아직 답변하지 않은 항목. 넘김 되돌리기가 가능한 SKIP_PENDING도 여기 남는다. */
    UNANSWERED,

    /** 답변을 마친 항목. 만료 전까지 목록에 남는다. */
    ANSWERED,
}
