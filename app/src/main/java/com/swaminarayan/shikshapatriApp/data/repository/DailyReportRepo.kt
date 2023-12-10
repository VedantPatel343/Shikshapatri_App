package com.swaminarayan.shikshapatriApp.data.repository

import com.swaminarayan.shikshapatriApp.data.data_source.DailyReportDAO
import com.swaminarayan.shikshapatriApp.domain.models.DailyAgnaFormReport
import kotlinx.coroutines.flow.Flow

class DailyReportRepo(
    private val dao: DailyReportDAO
) {

    fun getDailyReports(): Flow<List<DailyAgnaFormReport>> {
        return dao.getDailyReports()
    }

    suspend fun getDailyReportById(id: Long): DailyAgnaFormReport {
        return dao.getDailyReportById(id = id)
    }

    suspend fun upsertDailyReport(dailyAgnaFormReport: DailyAgnaFormReport) {
        return dao.upsertDailyReport(dailyAgnaFormReport)
    }

    suspend fun deleteDailyReport(dailyAgnaFormReport: DailyAgnaFormReport) {
        return dao.deleteDailyReport(dailyAgnaFormReport)
    }

}