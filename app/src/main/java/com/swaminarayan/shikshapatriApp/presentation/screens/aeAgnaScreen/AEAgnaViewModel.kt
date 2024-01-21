package com.swaminarayan.shikshapatriApp.presentation.screens.aeAgnaScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaminarayan.shikshapatriApp.constants.AGNA_ID
import com.swaminarayan.shikshapatriApp.data.repository.AgnaRepo
import com.swaminarayan.shikshapatriApp.domain.models.Agna
import com.swaminarayan.shikshapatriApp.utils.UiEvents
import com.swaminarayan.shikshapatriApp.utils.checkInt
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

    private val _rajipoPoints = MutableStateFlow("")
    val rajipoPoints = _rajipoPoints.map { it }.stateIn(
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


    private val _agnaError = MutableStateFlow(false)
    val agnaError = _agnaError.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )
    private val _rajipoPointsError = MutableStateFlow(false)
    val rajipoPointsError = _rajipoPointsError.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )
    private val _authorError = MutableStateFlow(false)
    val authorError = _authorError.map { it }.stateIn(
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
                    try {
                        if (agna != null) {
                            agnaId = id
                            _agna.value = agna.agna
                            _des.value = agna.description
                            _author.value = agna.author
                            _slokNo.value = agna.slokNo.toString()
                            _rajipoPoints.value = agna.rajipoPoints.toString()
                            _alwaysPalayChe.value = agna.alwaysPalayChe
                        }
                    }  catch (e: Exception) {
                        Log.i("exceptionCaught", "AEAgna VM: $e")
                    }
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
                _authorError.value = false
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
                            _rajipoPoints.value = event.points
                            _rajipoPointsError.value = false
                        } else {
                            _rajipoPointsError.value = true
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

            is AEAgnaEvents.OnSaveAgna -> {

                when {
                    _agna.value == "" -> {
                        viewModelScope.launch {
                            _agnaError.value = true
                            _uiEventFlow.emit(UiEvents.ShowToast("Agna can not be empty."))
                        }
                    }

                    _author.value == "" -> {
                        viewModelScope.launch {
                            _authorError.value = true
                            _uiEventFlow.emit(UiEvents.ShowToast("Author can not be empty."))
                        }
                    }

                    _rajipoPoints.value == "" -> {
                        viewModelScope.launch {
                            _rajipoPointsError.value = true
                            _uiEventFlow.emit(UiEvents.ShowToast("Rajipo Points can not be empty."))
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
                                rajipoPoints = rajipoPoints.value.toInt(),
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
                                rajipoPoints = rajipoPoints.value.toInt(),
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