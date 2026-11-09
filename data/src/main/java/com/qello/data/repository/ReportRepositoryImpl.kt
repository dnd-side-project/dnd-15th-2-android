package com.qello.data.repository

import com.qello.data.mapper.toDomain
import com.qello.data.remote.datasource.ReportDataSource
import com.qello.domain.model.ReportDetail
import com.qello.domain.model.ReportPage
import com.qello.domain.repository.ReportRepository
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val reportDataSource: ReportDataSource,
) : ReportRepository {
    override suspend fun getMyReports(cursor: String?, limit: Int): ReportPage =
        reportDataSource.getMyReports(cursor = cursor, limit = limit).toDomain()

    override suspend fun getReportDetail(reportId: Long): ReportDetail =
        reportDataSource.getReportDetail(reportId).toDomain()
}
