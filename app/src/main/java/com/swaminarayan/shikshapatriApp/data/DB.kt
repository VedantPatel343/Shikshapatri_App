package com.swaminarayan.shikshapatriApp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.swaminarayan.shikshapatriApp.utils.DailyAgnaListConverter
import com.swaminarayan.shikshapatriApp.utils.LocalDateConverter
import com.swaminarayan.shikshapatriApp.data.data_source.AgnaDAO
import com.swaminarayan.shikshapatriApp.data.data_source.DailyFormDAO
import com.swaminarayan.shikshapatriApp.data.data_source.NoteDAO
import com.swaminarayan.shikshapatriApp.domain.models.Agna
import com.swaminarayan.shikshapatriApp.domain.models.DailyForm
import com.swaminarayan.shikshapatriApp.domain.models.Note

@Database(entities = [Agna::class, DailyForm::class, Note::class], version = 6)
@TypeConverters(LocalDateConverter::class, DailyAgnaListConverter::class)
abstract class DB : RoomDatabase() {

    abstract val noteDAO: NoteDAO
    abstract val agnaDAO: AgnaDAO
    abstract val dailyFormDAO: DailyFormDAO

}