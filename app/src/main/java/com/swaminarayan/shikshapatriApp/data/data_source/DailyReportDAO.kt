package com.swaminarayan.shikshapatriApp.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.swaminarayan.shikshapatriApp.domain.models.DailyAgnaFormReport
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyReportDAO {

    @Query("SELECT * FROM DailyAgnaFormReport")
    fun getDailyReports(): Flow<List<DailyAgnaFormReport>>

    @Query("SELECT * FROM DailyAgnaFormReport WHERE id = :id")
    suspend fun getDailyReportById(id: Long): DailyAgnaFormReport

    @Upsert
    suspend fun upsertDailyReport(dailyAgnaFormReport: DailyAgnaFormReport)

    @Delete
    suspend fun deleteDailyReport(dailyAgnaFormReport: DailyAgnaFormReport)

}