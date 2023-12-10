package com.swaminarayan.shikshapatriApp.presentation.screens.aeGoalScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaminarayan.shikshapatriApp.constants.GOAL_ID
import com.swaminarayan.shikshapatriApp.data.repository.GoalsRepo
import com.swaminarayan.shikshapatriApp.domain.models.Goals
import com.swaminarayan.shikshapatriApp.utils.dateFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class AEGoalsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val goalsRepo: GoalsRepo
) : ViewModel() {


    private val _goal = MutableStateFlow("")
    val goal = _goal.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ""
    )
    private val _date = MutableStateFlow("")
    val date = _date.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ""
    )

    private val _goalError = MutableStateFlow(false)
    val goalError = _goalError.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )
    private val _isAchieved = MutableStateFlow(false)
    val isAchieved = _isAchieved.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )

    private var goalId = -1L

    init {
        savedStateHandle.get<Long>(GOAL_ID)?.let { id ->
            if (id != -1L) {
                viewModelScope.launch {
                    val goal = goalsRepo.getGoalsById(id)
                    goalId = id
                    _date.value = dateFormatter(goal.dateTime)
                    _goal.value = goal.goal
                    _isAchieved.value = goal.isAchieved
                }
            }
        }
    }


    fun onGoalSave() {
        if (goalId == -1L) {
            // Add New Goal
            val goal = Goals(
                goal = goal.value,
                dateTime = LocalDateTime.now(),
                isAchieved = isAchieved.value
            )
            viewModelScope.launch {
                goalsRepo.upsertGoals(goal)
            }

        } else {
            // Update goal
            viewModelScope.launch {
                val goal1 = goalsRepo.getGoalsById(goalId)
                val goal = Goals(
                    id = goalId,
                    dateTime = goal1.dateTime,
                    isAchieved = isAchieved.value,
                    goal = goal.value
                )
                goalsRepo.upsertGoals(goal)
            }
        }
    }

    fun onGoalChange(goal: String) {
        _goal.value = goal
    }

    fun isAchievedChange() {
        _isAchieved.value = !isAchieved.value
    }


}