package com.qello.domain.model

/**
 * 내 신고 목록의 한 페이지. nextCursor가 null이면 마지막 페이지다.
 */
data class ReportPage(
    val reports: List<ReportSummary>,
    val nextCursor: String?,
)
