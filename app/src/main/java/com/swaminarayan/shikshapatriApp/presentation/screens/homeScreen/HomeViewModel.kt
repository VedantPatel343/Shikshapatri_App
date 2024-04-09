package com.swaminarayan.shikshapatriApp.presentation.screens.homeScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaminarayan.shikshapatriApp.data.repository.DSRepo
import com.swaminarayan.shikshapatriApp.data.repository.DailyFormRepo
import com.swaminarayan.shikshapatriApp.data.repository.NoteRepo
import com.swaminarayan.shikshapatriApp.domain.models.Note
import com.swaminarayan.shikshapatriApp.utils.launchFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel @Inject constructor(
    private val dailyFormRepo: DailyFormRepo,
    private val notesRepo: NoteRepo,
    private val dsRepo: DSRepo
) : ViewModel() {

    private val _state = MutableStateFlow(
        initialUiState()
    )
    val state = combine(
        _state,
        fetchData()
    ) { internalState, _ ->
        internalState
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        initialUiState()
    )

    private fun fetchData() = launchFlow {
        val dates = getFirstLastDays()
        setUpList(dates.first, dates.second)

        dsRepo.getSLogan().onEach { slogan ->
            _state.update {
                it.copy(slogan = slogan)
            }
        }.launchIn(viewModelScope)
        notesRepo.getAllNotes().onEach { notes ->
            _state.update {
                it.copy(notes = notes)
            }
        }.launchIn(viewModelScope)
        dsRepo.getHomeFirstDay().onEach { date ->
            _state.update {
                it.copy(firstDay = LocalDate.parse(date))
            }
        }.launchIn(viewModelScope)
        dsRepo.getHomeLastDay().onEach { date ->
            _state.update {
                it.copy(lastDay = LocalDate.parse(date))
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.ChangeFlagValue -> {
                _state.update {
                    it.copy(flag = false)
                }
            }

            is HomeEvent.UpdateSlogan -> {
                viewModelScope.launch(Dispatchers.IO) {
                    dsRepo.updateSlogan(event.slogan)
                }
            }

            is HomeEvent.OnNextDateClicked -> {
                viewModelScope.launch {
                    setUpList(
                        firstDay = state.value.firstDay.plusDays(7),
                        lastDay = state.value.lastDay.plusDays(7)
                    )
                    _state.update {
                        it.copy(
                            firstDay = state.value.firstDay.plusDays(7),
                            lastDay = state.value.lastDay.plusDays(7)
                        )
                    }
                }
            }

            is HomeEvent.OnPrevDateClicked -> {
                viewModelScope.launch {
                    setUpList(
                        firstDay = state.value.firstDay.minusDays(7),
                        lastDay = state.value.lastDay.minusDays(7)
                    )
                    _state.update {
                        it.copy(
                            firstDay = state.value.firstDay.minusDays(7),
                            lastDay = state.value.lastDay.minusDays(7)
                        )
                    }
                }
            }
        }
    }

    suspend fun setUpList(firstDay: LocalDate, lastDay: LocalDate) {
        _state.update { state ->
            state.copy(
                dailyFormDateList = dailyFormRepo.getDailyFormBetweenDays(
                    firstDay = firstDay,
                    lastDay = lastDay
                ).map { it.date }
            )
        }
    }

    suspend fun getIdByDate(date: LocalDate): Long {
        return withContext(Dispatchers.IO) {
            val form = dailyFormRepo.getIdByDate(date)
            form?.id ?: -1L
        }
    }

    private fun getFirstLastDays(): Pair<LocalDate, LocalDate> {
        val today = LocalDate.now()
        val mondayValue = DayOfWeek.MONDAY.value
        val todayWeekDayValue = today.dayOfWeek.value

        val balance = todayWeekDayValue - mondayValue
        val firstDay = today.minusDays(balance.toLong())
        val lastDay = firstDay.plusDays(6)
        return Pair(firstDay, lastDay)
    }

    private fun initialUiState() =
        HomeUiState(
            firstDay = LocalDate.now(),
            lastDay = LocalDate.now(),
            slogan = "",
            notes = emptyList(),
            dailyFormDateList = emptyList(),
            flag = true
        )

    data class HomeUiState(
        val firstDay: LocalDate,
        val lastDay: LocalDate,
        val slogan: String,
        val notes: List<Note>,
        val dailyFormDateList: List<LocalDate>,
        val flag: Boolean
    )

}