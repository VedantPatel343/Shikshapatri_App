package com.swaminarayan.shikshapatriApp.data.repository

import com.swaminarayan.shikshapatriApp.data.data_source.DailyFormDAO
import com.swaminarayan.shikshapatriApp.domain.models.DailyForm
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class DailyFormRepo(
    private val dao: DailyFormDAO
) {

    suspend fun dailyFormList(): List<DailyForm> {
        return dao.dailyFormList()
    }

    suspend fun getDailyFormBetweenDays(firstDay: LocalDate, lastDay: LocalDate): List<DailyForm> {
        return dao.getDailyFormBetweenDays(firstDay, lastDay)
    }

    suspend fun getIdByDate(date: LocalDate): DailyForm? {
        return dao.getIdByDate(date)
    }

    suspend fun getDailyFormById(id: Long): DailyForm {
        return dao.getDailyReportById(id = id)
    }

    suspend fun upsertDailyForm(dailyForm: DailyForm) {
        return dao.upsertDailyReport(dailyForm)
    }

    suspend fun deleteDailyForm(dailyForm: DailyForm) {
        return dao.deleteDailyReport(dailyForm)
    }

}