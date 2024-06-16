package com.swaminarayan.shikshapatriApp.presentation.screens.reportScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaminarayan.shikshapatriApp.data.repository.AgnaRepo
import com.swaminarayan.shikshapatriApp.data.repository.DailyFormRepo
import com.swaminarayan.shikshapatriApp.domain.models.Agna
import com.swaminarayan.shikshapatriApp.domain.models.DailyAgna
import com.swaminarayan.shikshapatriApp.domain.models.DailyForm
import com.swaminarayan.shikshapatriApp.domain.models.ReportAgnaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.Month
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ReportViewModel @Inject constructor(
    private val dailyFormRepo: DailyFormRepo,
    private val agnaRepo: AgnaRepo
) : ViewModel() {

    private val _state = MutableStateFlow(
        initialUiState()
    )
    val state = combine(
        _state,
        _state
    ) { state, _ ->
        state
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        initialUiState()
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    totalAgnas = agnaRepo.agnas().size,
                    dailyFormList = dailyFormRepo.dailyFormList()
                )
            }
            setUpList(_state.value.dailyFormList)
        }
    }

    private fun setUpList(dailyForms: List<DailyForm>) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    monthlyForms = mutableListOf(),
                    reportAgnaItemList = mutableListOf(),
                    agnaPalanPoints = 0,
                    agnaLopPoints = 0,
                    remainingAgnaPoints = 0
                )
            }

            dailyForms.forEach { form ->
                if (form.date.month == _state.value.currentMonth && form.date.year == _state.value.date15.year) {
                    val list = _state.value.monthlyForms
                    list.add(form.date)
                    _state.update {
                        it.copy(monthlyForms = list)
                    }

                    form.dailyAgnas.forEach { dailyAgna ->
                        val agna = agnaRepo.getAgnaById(dailyAgna.id)
                        try {
                            if (agna != null) {
                                setReportItemModelList(
                                    agna = agna,
                                    dailyAgna = dailyAgna,
                                    points = getTotalPoints(
                                        agna,
                                        dailyAgna
                                    ),
                                    date = form.date,
                                    note = dailyAgna.note
                                )

                                when (dailyAgna.palai) {
                                    true -> {
                                        _state.update {
                                            it.copy(
                                                agnaPalanPoints = _state.value.agnaPalanPoints +
                                                        getTotalPoints(
                                                            agna,
                                                            dailyAgna
                                                        )
                                            )
                                        }
                                    }

                                    false -> {
                                        _state.update {
                                            it.copy(
                                                agnaLopPoints = _state.value.agnaLopPoints +
                                                        getTotalPoints(
                                                            agna,
                                                            dailyAgna
                                                        )
                                            )
                                        }
                                    }

                                    null -> {
                                        _state.update {
                                            it.copy(
                                                remainingAgnaPoints = _state.value.remainingAgnaPoints +
                                                        getTotalPoints(
                                                            agna,
                                                            dailyAgna
                                                        )
                                            )
                                        }
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            Log.i("exceptionCaught", "Report VM: $e")
                        }
                    }
                }
            }

        }
    }

    private fun getTotalPoints(agna: Agna, dailyAgna: DailyAgna): Long {
        return (agna.rajipoPoints * dailyAgna.count).toLong()
    }

    private fun setReportItemModelList(
        agna: Agna,
        dailyAgna: DailyAgna,
        points: Long,
        date: LocalDate,
        note: String
    ) {

        val item = _state.value.reportAgnaItemList.find { it.agnaId == agna.id }
        _state.value.reportAgnaItemList.removeAll { it.agnaId == agna.id }

        val reportItem = if (item != null) {
            val noteList = item.noteList.toMutableList()
            if (note.isNotEmpty()) {
                noteList.add(Pair(note, date))
            }

            ReportAgnaItem(
                agnaId = item.agnaId,
                agna = item.agna,
                agnaLopPoints = if (dailyAgna.palai == false) item.agnaLopPoints + points else item.agnaLopPoints,
                agnaPalanPoints = if (dailyAgna.palai == true) item.agnaPalanPoints + points else item.agnaPalanPoints,
                remainingAgnaPoints = if (dailyAgna.palai == null) item.remainingAgnaPoints + points else item.remainingAgnaPoints,
                totalPoints = item.totalPoints + points,
                noteList = noteList
            )
        } else {
            val noteList = if (note.isNotEmpty()) {
                listOf(Pair(note, date))
            } else {
                emptyList()
            }
            ReportAgnaItem(
                agnaId = agna.id,
                agna = agna.agna,
                agnaLopPoints = if (dailyAgna.palai == false) points else 0L,
                agnaPalanPoints = if (dailyAgna.palai == true) points else 0L,
                remainingAgnaPoints = if (dailyAgna.palai == null) points else 0L,
                totalPoints = points,
                noteList = noteList
            )
        }
        val list = _state.value.reportAgnaItemList
        list.add(reportItem)
        _state.update {
            it.copy(reportAgnaItemList = list)
        }
    }

    fun onNextMonthClicked() {
        update15Date(true)
        _state.update {
            it.copy(currentMonth = _state.value.currentMonth.plus(1))
        }
        setUpList(_state.value.dailyFormList)
    }

    private fun update15Date(onNextBtnClicked: Boolean) {
        val queryDate =
            if (onNextBtnClicked)
                _state.value.date15.plusDays(30)
            else
                _state.value.date15.minusDays(30)

        val balance = if (queryDate > _state.value.date15) {
            queryDate.dayOfMonth - _state.value.date15.dayOfMonth
        } else {
            _state.value.date15.dayOfMonth - queryDate.dayOfMonth
        }

        _state.update {
            it.copy(
                date15 = if (queryDate > _state.value.date15) {
                    queryDate.minusDays(balance.toLong())
                } else {
                    queryDate.plusDays(balance.toLong())
                }
            )
        }
    }

    fun onPreviousMonthClicked() {
        update15Date(false)
        _state.update {
            it.copy(currentMonth = _state.value.currentMonth.minus(1))
        }
        setUpList(_state.value.dailyFormList)
    }

    suspend fun getIdByDate(date: LocalDate): Long {
        return withContext(Dispatchers.IO) {
            dailyFormRepo.getIdByDate(date)?.id!!
        }
    }

    private fun initialUiState() =
        ReportUiState(
            totalAgnas = 0,
            agnaPalanPoints = 0,
            agnaLopPoints = 0,
            remainingAgnaPoints = 0,
            dailyFormList = emptyList(),
            currentMonth = LocalDate.now().month,
            today = LocalDate.now(),
            date15 = LocalDate.now(),
            monthlyForms = mutableListOf(),
            reportAgnaItemList = mutableListOf()
        )

    data class ReportUiState(
        val totalAgnas: Int,
        val agnaPalanPoints: Long,
        val agnaLopPoints: Long,
        val remainingAgnaPoints: Long,
        val dailyFormList: List<DailyForm>,
        val currentMonth: Month,
        val today: LocalDate,
        val date15: LocalDate,
        val monthlyForms: MutableList<LocalDate>,
        val reportAgnaItemList: MutableList<ReportAgnaItem>
    )

}