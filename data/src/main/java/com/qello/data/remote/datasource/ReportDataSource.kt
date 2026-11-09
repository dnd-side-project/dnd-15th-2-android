package com.qello.data.remote.datasource

import com.qello.data.remote.model.report.ReportDetailResponse
import com.qello.data.remote.model.report.ReportPageResponse

interface ReportDataSource {
    suspend fun getMyReports(cursor: String?, limit: Int): ReportPageResponse

    suspend fun getReportDetail(reportId: Long): ReportDetailResponse
}
