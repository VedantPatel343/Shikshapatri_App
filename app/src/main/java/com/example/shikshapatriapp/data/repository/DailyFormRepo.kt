package com.example.shikshapatriapp.data.repository

import com.example.shikshapatriapp.data.data_source.DailyFormDAO
import com.example.shikshapatriapp.domain.models.DailyForm
import com.example.shikshapatriapp.domain.relation.DailyFormWithDailyAgna
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

    suspend fun getDailyFormWithDailyAgnaByDailyFormId(dailyFormId: Long): List<DailyFormWithDailyAgna> {
        return dao.getDailyFormWithDailyAgnaByDailyFormId(dailyFormId)
    }

    suspend fun getDailyFormById(id: Long): DailyForm {
        return dao.getDailyReportById(id = id)
    }

    suspend fun upsertDailyForm(dailyForm: DailyForm): Long {
        return dao.upsertDailyReport(dailyForm)
    }

    suspend fun deleteDailyForm(dailyForm: DailyForm) {
        dao.deleteDailyReport(dailyForm)
    }

}