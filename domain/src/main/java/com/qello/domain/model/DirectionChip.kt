package com.qello.domain.model

/** 수신함 화면 상단의 방향별 필터 칩(0개인 방향은 서버가 아예 내려주지 않는다). */
data class DirectionChip(
    val segmentKey: String,
    val displayName: String,
    val sortOrder: Int,
    val count: Long,
)
