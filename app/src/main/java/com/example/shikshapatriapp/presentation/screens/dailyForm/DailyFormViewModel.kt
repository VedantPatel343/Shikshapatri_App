package com.example.shikshapatriapp.presentation.screens.dailyForm

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shikshapatriapp.constants.FORM_ID
import com.example.shikshapatriapp.constants.NAV_DATE
import com.example.shikshapatriapp.data.repository.AgnaRepo
import com.example.shikshapatriapp.data.repository.DailyAgnaRepo
import com.example.shikshapatriapp.data.repository.DailyFormRepo
import com.example.shikshapatriapp.domain.models.Agna
import com.example.shikshapatriapp.domain.models.DailyAgna
import com.example.shikshapatriapp.domain.models.DailyAgnaHelperClass
import com.example.shikshapatriapp.domain.models.DailyForm
import com.example.shikshapatriapp.utils.UiEvents
import com.swaminarayan.shikshapatriApp.utils.toDailyAgnaHelperClass
import com.swaminarayan.shikshapatriApp.utils.toListOfDailyAgna
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class DailyFormViewModel @Inject constructor(
    private val agnaRepo: AgnaRepo,
    private val dailyFormRepo: DailyFormRepo,
    private val dailyAgnaRepo: DailyAgnaRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(
        initialState()
    )
    val state = combine(
        _state,
        _state
    ) { state, _ ->
        state
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        initialState()
    )

    init {
        savedStateHandle.get<String>(NAV_DATE)?.let { date ->
            _state.update {
                it.copy(date = LocalDate.parse(date))
            }
        }
        savedStateHandle.get<Long>(FORM_ID)?.let { formId1 ->
            viewModelScope.launch {
                val agnas = agnaRepo.agnas()
                _state.update {
                    it.copy(
                        formId = formId1,
                        agnas = agnas
                    )
                }
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
            val dailyAgnas = dailyAgnaRepo.getDailyAgnaList(id)
            Log.i("DailyFormTesting", "getDailyFormAgnas: $dailyAgnas")

            _state.value.agnas.forEach { agna ->
                val da = getDailyAgna(agna.id, dailyAgnas)
                Log.i("DailyFormTesting", "getDailyFormAgnas: $da")

                if (da?.palai != null) {
                    val dailyAgna = agna.toDailyAgnaHelperClass(
                        dailyAgnaId = da.dailyAgnaId,
                        palai = da.palai,
                        count = da.count,
                        note = da.note
                    )
                    val list = _state.value.processedAgnas
                    list.add(dailyAgna)
                    _state.update { it.copy(processedAgnas = list) }

                } else {
                    val dailyAgna = agna.toDailyAgnaHelperClass(null, 1, "", null)
                    val list = _state.value.remainingAgnas
                    list.add(dailyAgna)
                    _state.update { it.copy(remainingAgnas = list) }
                }
            }
        }
    }

    private fun getDailyAgna(agnaId: Long, list: List<DailyAgna>): DailyAgna? {
        return list.find { it.agnaId == agnaId }
    }

    private fun listSetUp() {
        _state.value.agnas.forEach { agna ->
            if (agna.alwaysPalayChe) {
                val dailyAgna = agna.toDailyAgnaHelperClass(true, 1, "", null)
                val list = _state.value.processedAgnas
                list.add(dailyAgna)
                _state.update { it.copy(processedAgnas = list) }

            } else {
                val dailyAgna = agna.toDailyAgnaHelperClass(null, 1, "", null)
                val list = _state.value.remainingAgnas
                list.add(dailyAgna)
                _state.update { it.copy(remainingAgnas = list) }
            }
        }
    }

    private val _uiEventFlow = MutableSharedFlow<UiEvents>()
    val uiEventFlow = _uiEventFlow.asSharedFlow()

    fun onFormFilledClick(
        agnaPalanList: List<DailyAgnaHelperClass>,
        agnaLopList: List<DailyAgnaHelperClass>
    ) {
        val finalList = agnaPalanList.toMutableList()
        finalList.addAll(agnaLopList)
        finalList.addAll(_state.value.remainingAgnas)

        if (_state.value.formId == -1L) {
            viewModelScope.launch {
                val formId = dailyFormRepo.upsertDailyForm(DailyForm(date = _state.value.date))
                dailyAgnaRepo.upsertDailyAgnaList(
                    finalList.toListOfDailyAgna(
                        dailyFormId = formId,
                        isFormAlreadySaved = false
                    )
                )
                _uiEventFlow.emit(UiEvents.ShowToast("Form Saved."))
            }
        } else {
            val dailyForm = DailyForm(
                id = _state.value.formId!!,
                date = _state.value.date
            )
            viewModelScope.launch {
                dailyFormRepo.upsertDailyForm(dailyForm)
                dailyAgnaRepo.upsertDailyAgnaList(
                    finalList.toListOfDailyAgna(
                        dailyFormId = _state.value.formId!!,
                        isFormAlreadySaved = true
                    )
                )
                _uiEventFlow.emit(UiEvents.ShowToast("Form Saved."))
            }
        }
        _state.update {
            it.copy(isLoadingAnimeVisible = true)
        }
    }

    fun agnaProcessed(
        dailyAgna: DailyAgnaHelperClass,
        palai: Boolean,
        isAgnaProcessed: Boolean,
        count: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            if (isAgnaProcessed) {

                val processedAgna =
                    _state.value.processedAgnas.find { it.agnaId == dailyAgna.agnaId }
                if (
                    processedAgna?.palai == false && palai
                    || processedAgna?.palai == true && !palai
                ) {
                    val list = _state.value.processedAgnas
                    val index = list.indexOfFirst { it.agnaId == dailyAgna.agnaId }
                    list[index] = dailyAgna.copy(palai = palai, count = count)

                    _state.update { it.copy(processedAgnas = list) }

                }

            } else {
                val remainingAgnasList = _state.value.remainingAgnas
                remainingAgnasList.removeIf { it.agnaId == dailyAgna.agnaId }
                _state.update { it.copy(remainingAgnas = remainingAgnasList) }

                val processedAgnasList = _state.value.processedAgnas
                processedAgnasList.add(dailyAgna.copy(palai = palai, count = count))
                _state.update { it.copy(processedAgnas = processedAgnasList) }
            }
        }
    }

    suspend fun getIdByDate(): Long {
        return withContext(Dispatchers.IO) {
            dailyFormRepo.getIdByDate(_state.value.date)?.id!!
        }
    }

    fun onNoteAgnaChanged(noteAgna: DailyAgnaHelperClass?, isNoteAgnaProcessed: Boolean) {
        _state.update {
            it.copy(
                noteAgna = noteAgna,
                isNoteAgnaProcessed = isNoteAgnaProcessed
            )
        }
    }

    fun onCountChange(dailyAgna: DailyAgnaHelperClass, count: Int, agnaProcessed: Boolean) {
        val list = if (agnaProcessed) {
            _state.value.processedAgnas
        } else {
            _state.value.remainingAgnas
        }
        val index = list.indexOfFirst { it.agnaId == dailyAgna.agnaId }
        list[index] = dailyAgna.copy(count = count)
    }

    fun agnaNoteChanged(note: String) {
        val noteAgna = _state.value.noteAgna
        val list = if (_state.value.isNoteAgnaProcessed!!) {
            _state.value.processedAgnas
        } else {
            _state.value.remainingAgnas
        }

        val index = list.indexOfFirst { it.agnaId == noteAgna?.agnaId }
        list[index] = noteAgna?.copy(note = note)!!
    }

    private fun initialState() =
        DailyFormUiState(
            agnas = emptyList(),
            processedAgnas = mutableStateListOf(),
            remainingAgnas = mutableStateListOf(),
            isLoadingAnimeVisible = false,
            date = LocalDate.now(),
            formId = null,
            noteAgna = null,
            isNoteAgnaProcessed = false
        )

    data class DailyFormUiState(
        val agnas: List<Agna>,
        val processedAgnas: SnapshotStateList<DailyAgnaHelperClass>,
        val remainingAgnas: SnapshotStateList<DailyAgnaHelperClass>,
        val isLoadingAnimeVisible: Boolean,
        val date: LocalDate,
        val formId: Long?,
        val noteAgna: DailyAgnaHelperClass?,
        val isNoteAgnaProcessed: Boolean?
    )

}
