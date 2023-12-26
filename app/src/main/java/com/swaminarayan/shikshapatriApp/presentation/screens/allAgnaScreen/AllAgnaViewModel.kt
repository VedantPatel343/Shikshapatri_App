package com.swaminarayan.shikshapatriApp.presentation.screens.allAgnaScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaminarayan.shikshapatriApp.data.repository.AgnaRepo
import com.swaminarayan.shikshapatriApp.domain.models.Agna
import com.swaminarayan.shikshapatriApp.utils.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllAgnaViewModel @Inject constructor(
    private val repo: AgnaRepo
) : ViewModel() {

    private val _agnas: MutableStateFlow<List<Agna>> = MutableStateFlow(emptyList())
    val agnas = _agnas.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private val _uiEventFlow = MutableSharedFlow<UiEvents>()
    val uiEventFlow = _uiEventFlow.asSharedFlow()

    private var getAgnasJob: Job? = null

    init {
        getAgnas()
    }

    private fun getAgnas() {
        getAgnasJob?.cancel()
        getAgnasJob = repo.getAgnas()
            .onEach { _agnas.value = it }
            .launchIn(viewModelScope)
    }

    fun deleteAgna(agna: Agna) {
        viewModelScope.launch {
            repo.deleteAgna(agna)
            _uiEventFlow.emit(UiEvents.ShowToast("Agna deleted."))
        }
    }

    fun onStarClicked(agna: Agna) {
        val agna1 = Agna(
            id = agna.id,
            agna = agna.agna,
            description = agna.description,
            author = agna.author,
            slokNo = agna.slokNo,
            rajipoPoints = agna.rajipoPoints,
            alwaysPalayChe = agna.alwaysPalayChe,
            isStared = !agna.isStared
        )
        viewModelScope.launch {
            repo.upsertAgna(agna1)
        }
    }

}