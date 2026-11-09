package com.qello.data.remote.datasource.impl

import com.qello.data.remote.datasource.ReportDataSource
import com.qello.data.remote.model.common.ApiResponse
import com.qello.data.remote.model.report.ReportDetailResponse
import com.qello.data.remote.model.report.ReportPageResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import javax.inject.Inject

class ReportDataSourceImpl @Inject constructor(
    private val client: HttpClient,
) : ReportDataSource {
    override suspend fun getMyReports(cursor: String?, limit: Int): ReportPageResponse =
        client.get("reports/me") {
            cursor?.let { parameter("cursor", it) }
            parameter("limit", limit)
        }.body<ApiResponse<ReportPageResponse>>().data

    override suspend fun getReportDetail(reportId: Long): ReportDetailResponse =
        client.get("reports/$reportId").body<ApiResponse<ReportDetailResponse>>().data
}
