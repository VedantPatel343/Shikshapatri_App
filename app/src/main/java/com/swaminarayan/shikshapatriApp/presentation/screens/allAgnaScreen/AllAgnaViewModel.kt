package com.swaminarayan.shikshapatriApp.presentation.screens.allAgnaScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaminarayan.shikshapatriApp.data.repository.AgnaRepo
import com.swaminarayan.shikshapatriApp.domain.models.Agna
import com.swaminarayan.shikshapatriApp.utils.UiEvents
import com.swaminarayan.shikshapatriApp.utils.launchFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllAgnaViewModel @Inject constructor(
    private val repo: AgnaRepo
) : ViewModel() {

    private val _state = MutableStateFlow(
        AllAgnaUiState(emptyList())
    )
    val state = combine(
        _state,
        fetchData()
    ) { state, _ ->
        state
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        AllAgnaUiState(emptyList())
    )

    private fun fetchData() = launchFlow {
        repo.getAgnas().collect { agnas ->
            _state.update {
                it.copy(agnas = agnas)
            }
        }
    }

    private val _uiEventFlow = MutableSharedFlow<UiEvents>()
    val uiEventFlow = _uiEventFlow.asSharedFlow()

    fun deleteAgna(agna: Agna) {
        viewModelScope.launch {
            repo.deleteAgna(agna)
            _uiEventFlow.emit(UiEvents.ShowToast("Agna deleted."))
        }
    }

    data class AllAgnaUiState(
        val agnas: List<Agna>
    )

}