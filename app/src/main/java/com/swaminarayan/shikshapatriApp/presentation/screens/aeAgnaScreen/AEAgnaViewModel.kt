package com.swaminarayan.shikshapatriApp.presentation.screens.aeAgnaScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaminarayan.shikshapatriApp.constants.AGNA_ID
import com.swaminarayan.shikshapatriApp.utils.UiEvents
import com.swaminarayan.shikshapatriApp.utils.checkInt
import com.swaminarayan.shikshapatriApp.data.repository.AgnaRepo
import com.swaminarayan.shikshapatriApp.domain.models.Agna
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AEAgnaViewModel @Inject constructor(
    private val agnaRepo: AgnaRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _points = MutableStateFlow("")
    val points = _points.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ""
    )
    private val _slokNo = MutableStateFlow("")
    val slokNo = _slokNo.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ""
    )
    private val _agna = MutableStateFlow("")
    val agna = _agna.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ""
    )
    private val _des = MutableStateFlow("")
    val des = _des.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ""
    )
    private val _author = MutableStateFlow("")
    val author = _author.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ""
    )
    private val _alwaysPalayChe = MutableStateFlow(false)
    val alwaysPalayChe = _alwaysPalayChe.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )
    private val _isStared = MutableStateFlow(false)
    val isStared = _isStared.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )


    private val _agnaError = MutableStateFlow(false)
    val agnaError = _agnaError.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )

    private val _uiEventFlow = MutableSharedFlow<UiEvents>()
    val uiEventFlow = _uiEventFlow.asSharedFlow()

    private var agnaId = -1L

    init {
        savedStateHandle.get<Long>(AGNA_ID)?.let { id ->
            if (id != -1L) {
                viewModelScope.launch {
                    val agna = agnaRepo.getAgnaById(id)
                    agnaId = id
                    _agna.value = agna.agna
                    _des.value = agna.description
                    _author.value = agna.author
                    _slokNo.value = agna.slokNo.toString()
                    _points.value = agna.points.toString()
                    _isStared.value = agna.isStared
                    _alwaysPalayChe.value = agna.alwaysPalayChe
                }
            }
        }
    }


    fun onEvent(event: AEAgnaEvents) {
        when (event) {
            is AEAgnaEvents.OnAgnaChange -> {
                _agna.value = event.agna
                _agnaError.value = false
            }

            is AEAgnaEvents.OnDesChange -> {
                _des.value = event.des
            }

            is AEAgnaEvents.OnAuthorChange -> {
                _author.value = event.author
            }

            is AEAgnaEvents.OnSlokNoChange -> {
                viewModelScope.launch {
                    try {
                        val isValid = checkInt(event.slokNo)
                        if (isValid || event.slokNo == "") {
                            _slokNo.value = event.slokNo
                        } else {
                            _uiEventFlow.emit(UiEvents.ShowToast("Invalid Character."))
                        }
                    } catch (e: Exception) {
                        _uiEventFlow.emit(UiEvents.ShowToast("Invalid Character."))
                    }
                }
            }

            is AEAgnaEvents.OnPointsChange -> {
                viewModelScope.launch {
                    try {
                        val isValid = checkInt(event.points)
                        if (isValid || event.points == "") {
                            _points.value = event.points
                        } else {
                            _uiEventFlow.emit(UiEvents.ShowToast("Invalid Character."))
                        }
                    } catch (e: Exception) {
                        _uiEventFlow.emit(UiEvents.ShowToast("Invalid Character."))
                    }
                }
            }

            is AEAgnaEvents.OnAlwaysPalayCheChange -> {
                _alwaysPalayChe.value = event.alwaysPalayChe
            }

            is AEAgnaEvents.OnIsStaredChange -> {
                _isStared.value = event.isStared
            }

            is AEAgnaEvents.OnSaveAgna -> {

                when (_agna.value) {
                    "" -> {
                        viewModelScope.launch {
                            _uiEventFlow.emit(UiEvents.ShowToast("Agna can not be empty."))
                        }
                    }
                    else -> {
                        if (agnaId == -1L) {
                            // Add new Agna
                            val agna = Agna(
                                agna = agna.value,
                                description = des.value,
                                author = author.value,
                                slokNo = if (slokNo.value.isEmpty()) 0 else slokNo.value.toInt(),
                                points = if (points.value.isEmpty()) 0 else points.value.toInt(),
                                isStared = isStared.value,
                                alwaysPalayChe = alwaysPalayChe.value
                            )
                            viewModelScope.launch {
                                agnaRepo.upsertAgna(agna)
                                _uiEventFlow.emit(UiEvents.ShowToast("Agna Saved."))
                            }
                        } else {
                            // Update Agna
                            val agna = Agna(
                                id = agnaId,
                                agna = agna.value,
                                description = des.value,
                                author = author.value,
                                slokNo = if (slokNo.value.isEmpty()) 0 else slokNo.value.toInt(),
                                points = if (points.value.isEmpty()) 0 else points.value.toInt(),
                                isStared = isStared.value,
                                alwaysPalayChe = alwaysPalayChe.value
                            )
                            viewModelScope.launch {
                                agnaRepo.upsertAgna(agna)
                                _uiEventFlow.emit(UiEvents.ShowToast("Agna Saved."))
                            }
                        }
                    }
                }

            }
        }
    }

}