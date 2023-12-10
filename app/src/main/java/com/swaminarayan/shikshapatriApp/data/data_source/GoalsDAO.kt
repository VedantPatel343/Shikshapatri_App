package com.swaminarayan.shikshapatriApp.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.swaminarayan.shikshapatriApp.domain.models.Agna
import com.swaminarayan.shikshapatriApp.domain.models.Goals
import kotlinx.coroutines.flow.Flow

@Dao
interface GoalsDAO {

    @Query("SELECT * FROM Goals")
    fun getGoals(): Flow<List<Goals>>

    @Query("SELECT * FROM Goals WHERE id = :id")
    suspend fun getGoalsById(id: Long): Goals

    @Upsert
    suspend fun upsertGoals(goals: Goals)

    @Delete
    suspend fun deleteGoals(goals: Goals)

}