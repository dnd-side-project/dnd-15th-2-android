package com.qello.domain.repository

import com.qello.domain.model.ReportDetail
import com.qello.domain.model.ReportPage

interface ReportRepository {
    /** 내가 접수한 신고를 최근 접수순으로 가져온다. cursor를 생략하면 첫 페이지를, 이전 페이지의 nextCursor를 넘기면 다음 페이지를 가져온다. */
    suspend fun getMyReports(
        cursor: String? = null,
        limit: Int = 20,
    ): ReportPage

    /** 내가 접수한 신고 하나의 처리 상태를 조회한다. 남의 신고이거나 없으면 실패한다. */
    suspend fun getReportDetail(reportId: Long): ReportDetail
}
