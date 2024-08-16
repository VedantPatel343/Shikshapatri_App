package com.example.shikshapatriapp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.shikshapatriapp.data.data_source.AgnaDAO
import com.example.shikshapatriapp.data.data_source.DailyAgnaDAO
import com.example.shikshapatriapp.data.data_source.DailyFormDAO
import com.example.shikshapatriapp.data.data_source.NoteDAO
import com.example.shikshapatriapp.domain.models.Agna
import com.example.shikshapatriapp.domain.models.DailyAgna
import com.example.shikshapatriapp.domain.models.DailyForm
import com.example.shikshapatriapp.domain.models.Note
import com.example.shikshapatriapp.utils.LocalDateConverter

@Database(
    entities = [Note::class, DailyForm::class, Agna::class, DailyAgna::class],
    version = 1
)
@TypeConverters(LocalDateConverter::class)
abstract class Database : RoomDatabase() {

    abstract val noteDAO: NoteDAO
    abstract val dailyFormDAO: DailyFormDAO
    abstract val agnaDAO: AgnaDAO
    abstract val dailyAgnaDAO: DailyAgnaDAO

}