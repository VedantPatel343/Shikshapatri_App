package com.swaminarayan.shikshapatriApp.presentation.screens.homeScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaminarayan.shikshapatriApp.data.repository.DSRepo
import com.swaminarayan.shikshapatriApp.data.repository.DailyFormRepo
import com.swaminarayan.shikshapatriApp.data.repository.NoteRepo
import com.swaminarayan.shikshapatriApp.domain.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
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

    private val _slogan = MutableStateFlow("")
    val slogan = _slogan.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ""
    )

    private val _notes: MutableStateFlow<List<Note>> = MutableStateFlow(emptyList())
    val notes = _notes.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _firstDayOfWeek: MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now())
    val firstDayOfWeek = _firstDayOfWeek.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        LocalDate.now()
    )

    private val _lastDayOfWeek: MutableStateFlow<LocalDate> = MutableStateFlow(LocalDate.now())
    val lastDayOfWeek = _lastDayOfWeek.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        LocalDate.now()
    )

    private val _dailyFormDateList: MutableStateFlow<MutableList<LocalDate>> =
        MutableStateFlow(mutableListOf())
    val dailyFormDateList = _dailyFormDateList.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        mutableListOf()
    )

    private var notesJob: Job? = null
    private var firstDayJob: Job? = null
    private var lastDayJob: Job? = null
    private var sloganJob: Job? = null

    var flag = true

    init {
        viewModelScope.launch {
            getFirstDay()
            getLastDay()
            getSlogan()
            getNotes()

            val days = getFirstLastDays()
            setUpList(firstDay = days.first, lastDay = days.second)
        }
    }

    private fun getSlogan() {
        sloganJob?.cancel()
        sloganJob = dsRepo.getSLogan().onEach {
            _slogan.value = it
        }.launchIn(viewModelScope)
    }

    private fun getNotes() {
        notesJob?.cancel()
        notesJob = notesRepo.getAllNotes()
            .onEach { _notes.value = it }
            .launchIn(viewModelScope)
    }

    private fun getLastDay() {
        lastDayJob?.cancel()
        lastDayJob = dsRepo.getHomeLastDay().onEach {
            _lastDayOfWeek.value = LocalDate.parse(it)
        }.launchIn(viewModelScope)
    }

    private fun getFirstDay() {
        firstDayJob?.cancel()
        firstDayJob = dsRepo.getHomeFirstDay().onEach {
            _firstDayOfWeek.value = LocalDate.parse(it)
        }.launchIn(viewModelScope)
    }

    fun setUpList(firstDay: LocalDate, lastDay: LocalDate) {
        viewModelScope.launch(Dispatchers.IO) {
            _dailyFormDateList.value.clear()
            dailyFormRepo.getDailyFormBetweenDays(
                firstDay = firstDay,
                lastDay = lastDay
            ).forEach { form ->
                _dailyFormDateList.value.add(form.date)
            }
        }
    }

    suspend fun getIdByDate(date: LocalDate): Long {
        return withContext(Dispatchers.IO) {
            val form = dailyFormRepo.getIdByDate(date)
            form?.id ?: -1L
        }
    }

    fun onNextDateClicked() {
        setUpList(
            firstDay = _firstDayOfWeek.value.plusDays(7),
            lastDay = _lastDayOfWeek.value.plusDays(7)
        )
        _firstDayOfWeek.value = _firstDayOfWeek.value.plusDays(7)
        _lastDayOfWeek.value = _lastDayOfWeek.value.plusDays(7)
    }

    fun onPrevDateClicked() {
        setUpList(
            firstDay = _firstDayOfWeek.value.minusDays(7),
            lastDay = _lastDayOfWeek.value.minusDays(7)
        )
        _firstDayOfWeek.value = _firstDayOfWeek.value.minusDays(7)
        _lastDayOfWeek.value = _lastDayOfWeek.value.minusDays(7)
    }

    fun updateSloganText(slogan: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dsRepo.updateSlogan(slogan)
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

    fun changeFlagValue() {
        flag = false
    }

}