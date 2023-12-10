package com.swaminarayan.shikshapatriApp.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.swaminarayan.shikshapatriApp.domain.models.Agna
import kotlinx.coroutines.flow.Flow

@Dao
interface AgnaDAO {

    @Query("SELECT * FROM Agna ORDER BY id ASC")
    fun getAgnas(): Flow<List<Agna>>

    @Query("SELECT * FROM Agna WHERE id = :id")
    suspend fun getAgnaById(id: Long): Agna

    @Upsert
    suspend fun upsertAgna(agna: Agna)

    @Delete
    suspend fun deleteAgna(agna: Agna)

}