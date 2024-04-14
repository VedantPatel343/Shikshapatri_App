package com.swaminarayan.shikshapatriApp.presentation.screens.aeAgnaScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaminarayan.shikshapatriApp.constants.AGNA_ID
import com.swaminarayan.shikshapatriApp.data.repository.AgnaRepo
import com.swaminarayan.shikshapatriApp.utils.UiEvents
import com.swaminarayan.shikshapatriApp.utils.checkInt
import com.swaminarayan.shikshapatriApp.utils.launchFlow
import com.swaminarayan.shikshapatriApp.utils.toAgna
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
class AEAgnaViewModel @Inject constructor(
    private val agnaRepo: AgnaRepo,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(
        initialUiState()
    )
    val state = combine(
        _state,
        fetchData()
    ) { state, _ ->
        state
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        initialUiState()
    )

    private val _uiEventFlow = MutableSharedFlow<UiEvents>()
    val uiEventFlow = _uiEventFlow.asSharedFlow()

    private var agnaId: Long? = null

    private fun fetchData() = launchFlow {
        savedStateHandle.get<Long>(AGNA_ID)?.let { id ->
            if (id != -1L) {
                viewModelScope.launch {
                    val agna = agnaRepo.getAgnaById(id)
                    try {
                        if (agna != null) {
                            agnaId = id
                            _state.update {
                                it.copy(
                                    agna = agna.agna,
                                    des = agna.description,
                                    author = agna.author,
                                    rajipoPoints = agna.rajipoPoints.toString(),
                                    alwaysPalayChe = agna.alwaysPalayChe,
                                    isCounter = agna.isCounter
                                )
                            }
                        }
                    } catch (e: Exception) {
                        Log.i("exceptionCaught", "AEAgna VM: $e")
                    }
                }
            }
        }
    }


    fun onEvent(event: AEAgnaEvents) {
        when (event) {
            is AEAgnaEvents.OnAgnaChange -> {
                _state.update {
                    it.copy(
                        agna = event.agna,
                        agnaError = false
                    )
                }
            }

            is AEAgnaEvents.OnDesChange -> {
                _state.update {
                    it.copy(des = event.des)
                }
            }

            is AEAgnaEvents.OnAuthorChange -> {
                _state.update {
                    it.copy(author = event.author)
                }
            }

            is AEAgnaEvents.OnPointsChange -> {
                viewModelScope.launch {
                    try {
                        val isValid = checkInt(event.points)
                        if (isValid || event.points == "") {
                            _state.update {
                                it.copy(rajipoPoints = event.points)
                            }
                        } else {
                            _uiEventFlow.emit(UiEvents.ShowToast("Invalid Character."))
                        }
                    } catch (e: Exception) {
                        _uiEventFlow.emit(UiEvents.ShowToast("Invalid Character."))
                    }
                }
            }

            is AEAgnaEvents.IsAlwaysPalayCheChange -> {
                _state.update {
                    it.copy(alwaysPalayChe = event.alwaysPalayChe)
                }
            }

            is AEAgnaEvents.IsCounterChange -> {
                _state.update {
                    it.copy(isCounter = event.isCounter)
                }
            }

            is AEAgnaEvents.OnSaveAgna -> {
                when {
                    state.value.agna == "" -> {
                        viewModelScope.launch {
                            _state.update {
                                it.copy(agnaError = true)
                            }
                            _uiEventFlow.emit(UiEvents.ShowToast("Agna can not be empty."))
                        }
                    }

                    else -> {
                        val agna = state.value.toAgna(agnaId)
                        viewModelScope.launch {
                            agnaRepo.upsertAgna(agna)
                            _uiEventFlow.emit(UiEvents.ShowToast("Agna Saved."))
                        }
                    }
                }
            }
        }
    }


    private fun initialUiState() =
        AEAgnaUiState(
            rajipoPoints = "",
            agna = "",
            des = "",
            author = "",
            alwaysPalayChe = false,
            agnaError = false,
            isCounter = false
        )

    data class AEAgnaUiState(
        val rajipoPoints: String,
        val agna: String,
        val des: String,
        val author: String,
        val alwaysPalayChe: Boolean,
        val agnaError: Boolean,
        val isCounter: Boolean
    )

}