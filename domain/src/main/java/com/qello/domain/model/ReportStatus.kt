package com.qello.domain.model

/**
 * ACTIONED, NO_VIOLATION만 종결 상태이고 나머지는 아직 처리 중인 상태다.
 */
enum class ReportStatus {
    RECEIVED,
    AUTO_HIDDEN,
    UNDER_REVIEW,
    ACTIONED,
    NO_VIOLATION,
    MORE_INFO_REQUIRED,
}
