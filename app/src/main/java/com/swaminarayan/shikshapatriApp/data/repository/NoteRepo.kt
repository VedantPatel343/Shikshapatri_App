package com.swaminarayan.shikshapatriApp.data.repository

import com.swaminarayan.shikshapatriApp.data.data_source.NoteDAO
import com.swaminarayan.shikshapatriApp.domain.models.Note
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