package com.swaminarayan.shikshapatriApp.presentation.screens.dailyForm

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaminarayan.shikshapatriApp.constants.FORM_ID
import com.swaminarayan.shikshapatriApp.data.repository.AgnaRepo
import com.swaminarayan.shikshapatriApp.data.repository.DailyFormRepo
import com.swaminarayan.shikshapatriApp.domain.models.Agna
import com.swaminarayan.shikshapatriApp.domain.models.DailyAgna
import com.swaminarayan.shikshapatriApp.domain.models.DailyForm
import com.swaminarayan.shikshapatriApp.utils.UiEvents
import com.swaminarayan.shikshapatriApp.utils.getTotalScore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class DailyFormViewModel @Inject constructor(
    private val agnaRepo: AgnaRepo,
    private val dailyFormRepo: DailyFormRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var _agnas: List<Agna> = emptyList()

    val processedAgnas = mutableStateListOf<DailyAgna>()
    val remainingAgnas = mutableStateListOf<DailyAgna>()

    var liveScore: Int = 0

    var date: LocalDate = LocalDate.now()
    var id = -1L

    init {
        savedStateHandle.get<Long>(FORM_ID)?.let { formId ->
            viewModelScope.launch {
                id = formId
                if (formId != -1L) {
                    date = dailyFormRepo.getDailyFormById(formId).date
                    getDailyFormAgnas(formId)
                } else {
                    getAgnas()
                    listSetUp()
                }
            }
        }
    }

    private fun getDailyFormAgnas(id: Long) {
        viewModelScope.launch {
            processedAgnas.addAll(dailyFormRepo.getDailyFormById(id).dailyAgnas)
            updateLiveScore()
        }
    }

    private fun listSetUp() {
        _agnas.forEach { agna ->
            if (agna.alwaysPalayChe) {
                val dailyAgna = DailyAgna(
                    id = agna.id,
                    agna = agna.agna,
                    description = agna.description,
                    author = agna.author,
                    isStared = agna.isStared,
                    alwaysPalayChe = agna.alwaysPalayChe,
                    slokNo = agna.slokNo,
                    points = agna.rajipoPoints,
                    palai = true
                )
                liveScore += agna.rajipoPoints
                processedAgnas.add(dailyAgna)
            } else {
                val dailyAgna = DailyAgna(
                    id = agna.id,
                    agna = agna.agna,
                    description = agna.description,
                    author = agna.author,
                    isStared = agna.isStared,
                    alwaysPalayChe = agna.alwaysPalayChe,
                    slokNo = agna.slokNo,
                    points = agna.rajipoPoints,
                    palai = null
                )
                remainingAgnas.add(dailyAgna)
            }
        }
    }


    private suspend fun getAgnas() {
        _agnas = agnaRepo.agnas()
    }

    private val _uiEventFlow = MutableSharedFlow<UiEvents>()
    val uiEventFlow = _uiEventFlow.asSharedFlow()

    fun onFormFilledClick() {
        if (remainingAgnas.isNotEmpty()) {
            viewModelScope.launch {
                _uiEventFlow.emit(UiEvents.ShowToast("First fill whole form."))
            }
        } else {

            val totalScore = getTotalScore(processedAgnas)
            if (id == -1L) {
                val dailyForm = DailyForm(
                    dailyAgnas = processedAgnas.toList(),
                    totalScore = totalScore,
                    date = date
                )
                viewModelScope.launch {
                    dailyFormRepo.upsertDailyForm(dailyForm)
                    _uiEventFlow.emit(UiEvents.ShowToast("Form Saved."))
                }
            } else {
                val dailyForm = DailyForm(
                    id = id,
                    dailyAgnas = processedAgnas.toList(),
                    totalScore = totalScore,
                    date = date
                )
                viewModelScope.launch {
                    dailyFormRepo.upsertDailyForm(dailyForm)
                    _uiEventFlow.emit(UiEvents.ShowToast("Form Saved."))
                }
            }

        }

    }

    fun agnaProcessed(dailyAgna: DailyAgna, palai: Boolean) {
        val processedAgna = processedAgnas.find { it.id == dailyAgna.id }
        val isAgnaAdded = processedAgnas.contains(dailyAgna)
        if ((processedAgna?.palai == true && !palai)) {
            liveScore -= dailyAgna.points
        }
        if ((!isAgnaAdded && palai) || (processedAgna?.palai == false && palai)) {
            liveScore += dailyAgna.points
        }

        if (!isAgnaAdded || (processedAgna?.palai == false && palai) || (processedAgna?.palai == true && !palai)) {
            processedAgnas.removeIf { it.id == dailyAgna.id }
            processedAgnas.add(dailyAgna.copy(palai = palai))
        }
        remainingAgnas.removeIf { it.id == dailyAgna.id }
    }

    private fun updateLiveScore() {
        processedAgnas.toList()
            .filter { it.palai == true }
            .forEach {
                liveScore += it.points
            }
    }

}