package com.swaminarayan.shikshapatriApp.presentation.screens.agnaForm

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaminarayan.shikshapatriApp.data.repository.AgnaRepo
import com.swaminarayan.shikshapatriApp.data.repository.DailyReportRepo
import com.swaminarayan.shikshapatriApp.domain.models.Agna
import com.swaminarayan.shikshapatriApp.domain.models.DailyAgnaFormReport
import com.swaminarayan.shikshapatriApp.domain.models.DailyAgnas
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DailyAgnaFormViewModel @Inject constructor(
    private val agnaRepo: AgnaRepo,
    private val dailyReportRepo: DailyReportRepo
) : ViewModel() {

    private val _agnas: MutableStateFlow<List<Agna>> = MutableStateFlow(emptyList())
    private val agnas = _agnas.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val processedAgnas = mutableStateListOf<DailyAgnas>()
    val remainingAgnas = mutableStateListOf<DailyAgnas>()


    private var getAgnasJob: Job? = null

    init {
        getAgnas()
        listSetUp()
    }

    private fun listSetUp() {
        agnas.value.filter { it.alwaysPalayChe }.forEach {
            val dailyAgnas = DailyAgnas(
                id = it.id,
                agna = it.agna,
                description = it.description,
                author = it.author,
                isStared = it.isStared,
                alwaysPalayChe = it.alwaysPalayChe,
                slokNo = it.slokNo,
                points = it.points,
                palai = true
            )
            processedAgnas.add(dailyAgnas)
        }
        agnas.value.filter { !it.alwaysPalayChe }.forEach { agna ->
            val dailyAgnas = DailyAgnas(
                id = agna.id,
                agna = agna.agna,
                description = agna.description,
                author = agna.author,
                isStared = agna.isStared,
                alwaysPalayChe = agna.alwaysPalayChe,
                slokNo = agna.slokNo,
                points = agna.points,
                palai = null
            )
            remainingAgnas.add(dailyAgnas)
        }
    }

    private fun getAgnas() {
        getAgnasJob?.cancel()
        getAgnasJob = agnaRepo.getAgnas()
            .onEach { _agnas.value = it }
            .launchIn(viewModelScope)
    }

    fun onFormFilledClick() {
        // TODO -> Form is finally filled, now add this data to database but first check that remainingAgnas list is empty or not,
        //TODO     if not then give warning to fill full form then save it, else if it is empty then save it to db and pass processedList to db.

//        val finalReport = DailyAgnaFormReport(
//            dateTime = ,
//            totalScore = ,
//            dailyAgnas =
//        )

    }

    fun agnaProcessed(dailyAgna: DailyAgnas, palai: Boolean) {
        processedAgnas.add(dailyAgna.copy(palai = palai))
        remainingAgnas.removeIf { it.id == dailyAgna.id }
    }

}