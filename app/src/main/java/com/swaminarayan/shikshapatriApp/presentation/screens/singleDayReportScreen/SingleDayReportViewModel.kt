package com.swaminarayan.shikshapatriApp.presentation.screens.singleDayReportScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaminarayan.shikshapatriApp.constants.FORM_ID
import com.swaminarayan.shikshapatriApp.data.repository.AgnaRepo
import com.swaminarayan.shikshapatriApp.data.repository.DailyFormRepo
import com.swaminarayan.shikshapatriApp.domain.models.Agna
import com.swaminarayan.shikshapatriApp.domain.models.DailyAgna
import com.swaminarayan.shikshapatriApp.domain.models.DailyForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class SingleDayReportViewModel @Inject constructor(
    private val dailyFormRepo: DailyFormRepo,
    private val agnaRepo: AgnaRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(initialUiState())
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
        savedStateHandle.get<Long>(FORM_ID)?.let {
            viewModelScope.launch {
                _state.update {
                    it.copy(totalAgnas = agnaRepo.agnas().size.toLong())
                }
                setUpList(it)
            }
        }
    }

    private fun setUpList(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val form = dailyFormRepo.getDailyFormById(id)
            _state.update {
                it.copy(date = form.date)
            }

            form.dailyAgnas.forEach { dailyAgna ->
                val agna = agnaRepo.getAgnaById(dailyAgna.id)
                try {
                    if (agna != null) {
                        if (dailyAgna.palai == true) {
                            val list = _state.value.agnaPalanList.toMutableList()
                            list.add(agna)
                            _state.update {
                                it.copy(
                                    totalAgnaPalanPoints = _state.value.totalAgnaPalanPoints + agna.rajipoPoints,
                                    agnaPalanList = list.toList()
                                )
                            }
                        } else {
                            val list = _state.value.agnaLopList.toMutableList()
                            list.add(agna)
                            _state.update {
                                it.copy(
                                    totalAgnaLopPoints = _state.value.totalAgnaLopPoints + agna.rajipoPoints,
                                    agnaLopList = list.toList()
                                )
                            }
                        }
                    } else {
                        removeAgnaFromDailyForm(form, dailyAgna)
                    }
                } catch (e: Exception) {
                    Log.i("exceptionCaught", "SingleDayReport VM: $e")
                }
            }
            _state.update {
                it.copy(
                    remainingAgna = _state.value.totalAgnas -
                            (_state.value.agnaPalanList.size + _state.value.agnaLopList.size)
                )
            }
        }
    }

    private fun removeAgnaFromDailyForm(
        form: DailyForm,
        dailyAgna: DailyAgna
    ) {
        val list = mutableListOf<DailyAgna>()
        form.dailyAgnas.forEach {
            if (it.id != dailyAgna.id) {
                list.add(dailyAgna)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            dailyFormRepo.upsertDailyForm(form.copy(dailyAgnas = list))
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
private fun initialUiState() = SDReportUiState(
    totalAgnas = 0L,
    date = LocalDate.now(),
    agnaPalanList = emptyList(),
    agnaLopList = emptyList(),
    totalAgnaPalanPoints = 0L,
    totalAgnaLopPoints = 0L,
    remainingAgna = 0L
)

data class SDReportUiState(
    var totalAgnas: Long,
    var date: LocalDate,
    var agnaPalanList: List<Agna>,
    var agnaLopList: List<Agna>,
    var totalAgnaPalanPoints: Long,
    var totalAgnaLopPoints: Long,
    var remainingAgna: Long
)