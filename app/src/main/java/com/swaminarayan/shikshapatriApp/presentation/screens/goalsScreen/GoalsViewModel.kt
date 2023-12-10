package com.swaminarayan.shikshapatriApp.presentation.screens.goalsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaminarayan.shikshapatriApp.data.repository.GoalsRepo
import com.swaminarayan.shikshapatriApp.domain.models.Goals
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoalsViewModel @Inject constructor(
    private val goalsRepo: GoalsRepo
) : ViewModel() {

    private val _goals: MutableStateFlow<List<Goals>> = MutableStateFlow(emptyList())
    val goals = _goals.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private var getGoalsJob: Job? = null

    init {
        getGoals()
    }

    private fun getGoals() {
        getGoalsJob?.cancel()
        getGoalsJob = goalsRepo.getGoals()
            .onEach { _goals.value = it }
            .launchIn(viewModelScope)
    }

    fun deleteGoals(goal: Goals) {
        viewModelScope.launch {
            goalsRepo.deleteGoals(goal)
        }
    }

}