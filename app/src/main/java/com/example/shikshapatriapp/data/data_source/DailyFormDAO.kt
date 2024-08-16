package com.example.shikshapatriapp.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.shikshapatriapp.domain.models.DailyForm
import com.example.shikshapatriapp.domain.relation.DailyFormWithDailyAgna
import java.time.LocalDate

@Dao
interface DailyFormDAO {

    @Query("SELECT * FROM DailyForm ORDER BY date ASC")
    suspend fun dailyFormList(): List<DailyForm>

    @Query("SELECT * FROM DailyForm WHERE date BETWEEN :firstDay AND :lastDay ORDER BY date ASC")
    suspend fun getDailyFormBetweenDays(firstDay: LocalDate, lastDay: LocalDate): List<DailyForm>

    @Transaction
    @Query("SELECT * FROM DailyForm WHERE id = :dailyFormId")
    fun getDailyFormWithDailyAgnaByDailyFormId(dailyFormId: Long): List<DailyFormWithDailyAgna>

    @Query("SELECT * FROM DailyForm WHERE id = :id")
    suspend fun getDailyReportById(id: Long): DailyForm

    @Query("SELECT * FROM DailyForm WHERE date = :date")
    suspend fun getIdByDate(date: LocalDate): DailyForm?

    @Upsert
    suspend fun upsertDailyReport(dailyForm: DailyForm): Long

    @Delete
    suspend fun deleteDailyReport(dailyForm: DailyForm)

}