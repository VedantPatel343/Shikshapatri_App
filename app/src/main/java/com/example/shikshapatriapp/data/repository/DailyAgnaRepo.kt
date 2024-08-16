package com.example.shikshapatriapp.data.repository

import com.example.shikshapatriapp.data.data_source.DailyAgnaDAO
import com.example.shikshapatriapp.domain.models.DailyAgna

class DailyAgnaRepo(
    private val dao: DailyAgnaDAO
) {

    suspend fun getDailyAgnaList(dailyFormId: Long): List<DailyAgna> {
        return dao.getDailyAgnaByDailyFormId(dailyFormId)
    }

    suspend fun upsertDailyAgna(dailyAgna: DailyAgna) {
        dao.upsertDailyAgna(dailyAgna)
    }

    suspend fun upsertDailyAgnaList(dailyAgnas: List<DailyAgna>) {
        dao.upsertDailyAgnaList(dailyAgnas)
    }

    suspend fun deleteDailyAgna(dailyAgna: DailyAgna) {
        dao.deleteDailyAgna(dailyAgna)
    }

}