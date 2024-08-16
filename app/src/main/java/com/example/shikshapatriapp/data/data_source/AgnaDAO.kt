package com.example.shikshapatriapp.data.data_source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.shikshapatriapp.domain.models.Agna
import kotlinx.coroutines.flow.Flow

@Dao
interface AgnaDAO {

    //    @Query("SELECT * FROM Agna")
    @Query("SELECT * FROM Agna ORDER BY id ASC")
    fun getAgnas(): Flow<List<Agna>>

    @Query("SELECT * FROM Agna ORDER BY id ASC")
    suspend fun agnas(): List<Agna>

    @Query("SELECT * FROM Agna WHERE id = :id")
    suspend fun getAgnaById(id: Long): Agna?

    @Upsert
    suspend fun upsertAgna(agna: Agna)

    @Delete
    suspend fun deleteAgna(agna: Agna)

}