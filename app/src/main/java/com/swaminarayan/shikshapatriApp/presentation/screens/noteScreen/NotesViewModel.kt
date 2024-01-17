package com.swaminarayan.shikshapatriApp.presentation.screens.noteScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaminarayan.shikshapatriApp.data.repository.AgnaRepo
import com.swaminarayan.shikshapatriApp.data.repository.NoteRepo
import com.swaminarayan.shikshapatriApp.domain.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesRepo: NoteRepo
): ViewModel() {

    private val _notes: MutableStateFlow<List<Note>> = MutableStateFlow(emptyList())
    val notes = _notes.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    private var notesJob: Job? = null

    init {
        getNotes()
    }

    private fun getNotes() {
        notesJob?.cancel()
        notesJob = notesRepo.getAllNotes()
            .onEach { _notes.value = it }
            .launchIn(viewModelScope)
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch (Dispatchers.IO) {
            notesRepo.deleteNote(note)
        }
    }

}