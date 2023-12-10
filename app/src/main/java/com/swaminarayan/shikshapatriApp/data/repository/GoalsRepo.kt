package com.swaminarayan.shikshapatriApp.data.repository

import com.swaminarayan.shikshapatriApp.data.data_source.GoalsDAO
import com.swaminarayan.shikshapatriApp.domain.models.Goals
import kotlinx.coroutines.flow.Flow

class GoalsRepo(
    private val dao: GoalsDAO
) {

    fun getGoals(): Flow<List<Goals>> {
        return dao.getGoals()
    }

    suspend fun getGoalsById(id: Long): Goals {
        return dao.getGoalsById(id = id)
    }

    suspend fun upsertGoals(goals: Goals) {
        dao.upsertGoals(goals)
    }

    suspend fun deleteGoals(goals: Goals) {
        dao.deleteGoals(goals)
    }

}