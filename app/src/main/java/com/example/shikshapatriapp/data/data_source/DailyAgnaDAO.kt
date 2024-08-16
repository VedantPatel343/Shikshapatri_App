package com.example.shikshapatriapp.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.shikshapatriapp.domain.models.DailyAgna

@Dao
interface DailyAgnaDAO {

    @Query("SELECT * FROM dailyagna WHERE dailyFormId = :id ORDER BY agnaId ASC")
    suspend fun getDailyAgnaByDailyFormId(id: Long): List<DailyAgna>

    @Transaction
    @Upsert
    suspend fun upsertDailyAgna(dailyAgna: DailyAgna)

    @Transaction
    @Upsert
    suspend fun upsertDailyAgnaList(dailyAgnas: List<DailyAgna>)

    @Transaction
    @Delete
    suspend fun deleteDailyAgna(dailyAgna: DailyAgna)

}