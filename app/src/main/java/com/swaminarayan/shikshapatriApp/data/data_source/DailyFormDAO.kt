package com.swaminarayan.shikshapatriApp.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.swaminarayan.shikshapatriApp.domain.models.DailyForm
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface DailyFormDAO {

    @Query("SELECT * FROM DailyForm")
    fun getDailyReports(): Flow<List<DailyForm>>

    @Query("SELECT * FROM DailyForm")
    suspend fun dailyFormList(): List<DailyForm>

    @Query("SELECT * FROM DailyForm WHERE id = :id")
    suspend fun getDailyReportById(id: Long): DailyForm

    @Query("SELECT * FROM DailyForm WHERE date = :date")
    suspend fun getIdByDate(date: LocalDate): DailyForm?

    @Upsert
    suspend fun upsertDailyReport(dailyForm: DailyForm)

    @Delete
    suspend fun deleteDailyReport(dailyForm: DailyForm)

}