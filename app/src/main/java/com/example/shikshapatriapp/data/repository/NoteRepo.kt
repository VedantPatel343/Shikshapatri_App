package com.example.shikshapatriapp.data.repository

import com.example.shikshapatriapp.data.data_source.NoteDAO
import com.example.shikshapatriapp.domain.models.Note
import kotlinx.coroutines.flow.Flow

class NoteRepo(
    private val dao: NoteDAO
) {
    fun getAllNotes(): Flow<List<Note>> {
        return dao.getAllNotes()
    }

    suspend fun upsertNote(note: Note) {
        dao.upsertNote(note)
    }

    suspend fun deleteNote(note: Note) {
        dao.deleteNote(note)
    }

    suspend fun getNoteByID(id: Long): Note {
        return dao.getNoteById(id)
    }
}