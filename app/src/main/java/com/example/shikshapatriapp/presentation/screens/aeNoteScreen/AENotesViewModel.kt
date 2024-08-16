package com.example.shikshapatriapp.presentation.screens.aeNoteScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shikshapatriapp.constants.NOTE_ID
import com.example.shikshapatriapp.data.repository.NoteRepo
import com.example.shikshapatriapp.domain.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AENotesViewModel @Inject constructor(
    private val repo: NoteRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _des: MutableStateFlow<String> = MutableStateFlow("")
    val des = _des.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ""
    )

    private var noteId = -1L

    init {
        savedStateHandle.get<Long>(NOTE_ID)?.let { id ->
            if (id != -1L) {
                noteId = id
                viewModelScope.launch {
                    val note = repo.getNoteByID(id)
                    _des.value = note.des
                }
            }
        }
    }

    fun onSaveNote() {
        if (noteId != -1L) {
            val note = Note(
                id = noteId,
                des = des.value
            )
            viewModelScope.launch {
                repo.upsertNote(note)
            }
        } else {
            val note = Note(
                des = des.value
            )
            viewModelScope.launch {
                repo.upsertNote(note)
            }
        }
    }

    fun desChanged(it: String) {
        _des.value = it
    }

}