package com.example.shikshapatriapp.data.repository

import com.example.shikshapatriapp.data.data_source.AgnaDAO
import com.example.shikshapatriapp.domain.models.Agna
import kotlinx.coroutines.flow.Flow

class AgnaRepo(
    private val dao: AgnaDAO
) {

    fun getAgnas(): Flow<List<Agna>> {
        return dao.getAgnas()
    }

    suspend fun agnas(): List<Agna> {
        return dao.agnas()
    }

    suspend fun getAgnaById(id: Long): Agna? {
        return dao.getAgnaById(id = id)
    }

    suspend fun upsertAgna(agna: Agna) {
        dao.upsertAgna(agna)
    }

    suspend fun deleteAgna(agna: Agna) {
        dao.deleteAgna(agna)
    }

}