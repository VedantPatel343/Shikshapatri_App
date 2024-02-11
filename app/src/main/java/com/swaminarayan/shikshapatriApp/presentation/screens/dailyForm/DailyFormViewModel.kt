package com.swaminarayan.shikshapatriApp.presentation.screens.dailyForm

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaminarayan.shikshapatriApp.constants.FORM_ID
import com.swaminarayan.shikshapatriApp.constants.NAV_DATE
import com.swaminarayan.shikshapatriApp.data.repository.AgnaRepo
import com.swaminarayan.shikshapatriApp.data.repository.DailyFormRepo
import com.swaminarayan.shikshapatriApp.domain.models.Agna
import com.swaminarayan.shikshapatriApp.domain.models.DailyAgna
import com.swaminarayan.shikshapatriApp.domain.models.DailyAgnaHelperClass
import com.swaminarayan.shikshapatriApp.domain.models.DailyForm
import com.swaminarayan.shikshapatriApp.utils.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    val processedAgnas = mutableStateListOf<DailyAgnaHelperClass>()
    val remainingAgnas = mutableStateListOf<DailyAgnaHelperClass>()

    var isLoadingAnimationVisible = false

    private val _liveScore: MutableStateFlow<Int> = MutableStateFlow(0)
    val liveScore = _liveScore.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )

    var date: LocalDate = LocalDate.now()
    var formId = -1L

    init {
        savedStateHandle.get<String>(NAV_DATE)?.let {
            date = LocalDate.parse(it)
        }
        savedStateHandle.get<Long>(FORM_ID)?.let { formId1 ->
            viewModelScope.launch {
                formId = formId1
                getAgnas()
                if (formId1 != -1L) {
                    getDailyFormAgnas(formId1)
                } else {
                    listSetUp()
                }
            }
        }
    }

    private fun getDailyFormAgnas(id: Long) {
        viewModelScope.launch {

            val list = dailyFormRepo.getDailyFormById(id).dailyAgnas
            val dailyAgnaIds = mutableListOf<Long>()
            list.forEach {
                dailyAgnaIds.add(it.id)
            }

            _agnas.forEach { agna ->
                if (dailyAgnaIds.contains(agna.id)) {
                    val agnaPalai = agnaPalai(agna.id, list)
                    val dailyAgna = DailyAgnaHelperClass(
                        id = agna.id,
                        agna = agna.agna,
                        description = agna.description,
                        rajipoPoints = agna.rajipoPoints,
                        slokNo = agna.slokNo,
                        author = agna.author,
                        alwaysPalayChe = agna.alwaysPalayChe,
                        palai = agnaPalai
                    )
                    if (agnaPalai == true) {
                        _liveScore.value += agna.rajipoPoints
                    }
                    processedAgnas.add(dailyAgna)
                } else {
                    val dailyAgna = DailyAgnaHelperClass(
                        id = agna.id,
                        agna = agna.agna,
                        description = agna.description,
                        rajipoPoints = agna.rajipoPoints,
                        author = agna.author,
                        slokNo = agna.slokNo,
                        alwaysPalayChe = agna.alwaysPalayChe,
                        palai = null
                    )
                    remainingAgnas.add(dailyAgna)
                }
            }
        }
    }

    private fun agnaPalai(agnaId: Long, list: List<DailyAgna>): Boolean? {
        val agna = list.find { it.id == agnaId }
        return agna?.palai
    }

    private fun listSetUp() {
        _agnas.forEach { agna ->
            if (agna.alwaysPalayChe) {
                val dailyAgna = DailyAgnaHelperClass(
                    id = agna.id,
                    agna = agna.agna,
                    description = agna.description,
                    author = agna.author,
                    alwaysPalayChe = true,
                    slokNo = agna.slokNo,
                    rajipoPoints = agna.rajipoPoints,
                    palai = true
                )
                _liveScore.value += agna.rajipoPoints
                processedAgnas.add(dailyAgna)
            } else {
                val dailyAgna = DailyAgnaHelperClass(
                    id = agna.id,
                    agna = agna.agna,
                    description = agna.description,
                    author = agna.author,
                    alwaysPalayChe = false,
                    slokNo = agna.slokNo,
                    rajipoPoints = agna.rajipoPoints,
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

            if (formId == -1L) {
                val dailyForm = DailyForm(
                    dailyAgnas = getDailyAgnaList(processedAgnas.toList()),
                    date = date
                )
                viewModelScope.launch {
                    dailyFormRepo.upsertDailyForm(dailyForm)
                    _uiEventFlow.emit(UiEvents.ShowToast("Form Saved."))
                }
            } else {
                val dailyForm = DailyForm(
                    id = formId,
                    dailyAgnas = getDailyAgnaList(processedAgnas.toList()),
                    date = date
                )
                viewModelScope.launch {
                    dailyFormRepo.upsertDailyForm(dailyForm)
                    _uiEventFlow.emit(UiEvents.ShowToast("Form Saved."))
                }
            }

            isLoadingAnimationVisible = true

        }
    }

    private fun getDailyAgnaList(list: List<DailyAgnaHelperClass>): List<DailyAgna> {
        val dailyAgnas: MutableList<DailyAgna> = mutableListOf()
        list.forEach {
            val dA = DailyAgna(id = it.id, palai = it.palai)
            dailyAgnas.add(dA)
        }
        return dailyAgnas
    }

    fun agnaProcessed(
        dailyAgna: DailyAgnaHelperClass,
        palai: Boolean,
        isAgnaProcessed: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isAgnaProcessed) {

                val processedAgna = processedAgnas.find { it.id == dailyAgna.id }
                var isValid = false
                if (processedAgna?.palai == false && palai) {
                    isValid = true
                    _liveScore.value += dailyAgna.rajipoPoints
                } else if (processedAgna?.palai == true && !palai) {
                    isValid = true
                    _liveScore.value -= dailyAgna.rajipoPoints
                }

                if (isValid) {
                    processedAgnas.removeIf { it.id == processedAgna?.id }
                    processedAgnas.add(dailyAgna.copy(palai = palai))
                }

            } else {

                if (palai) {
                    _liveScore.value += dailyAgna.rajipoPoints
                } else {
                    _liveScore.value -= dailyAgna.rajipoPoints
                }
                remainingAgnas.removeIf { it.id == dailyAgna.id }
                processedAgnas.add(dailyAgna.copy(palai = palai))
            }
        }
    }

    suspend fun getIdByDate(): Long {
        return withContext(Dispatchers.IO) {
            dailyFormRepo.getIdByDate(date)?.id!!
        }
    }
}
