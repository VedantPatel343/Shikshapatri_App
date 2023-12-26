package com.swaminarayan.shikshapatriApp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.swaminarayan.shikshapatriApp.utils.DailyAgnaListConverter
import com.swaminarayan.shikshapatriApp.utils.LocalDateConverter
import com.swaminarayan.shikshapatriApp.data.data_source.AgnaDAO
import com.swaminarayan.shikshapatriApp.data.data_source.DailyFormDAO
import com.swaminarayan.shikshapatriApp.domain.models.Agna
import com.swaminarayan.shikshapatriApp.domain.models.DailyForm

@Database(entities = [Agna::class, DailyForm::class], version = 3)
@TypeConverters(LocalDateConverter::class, DailyAgnaListConverter::class)
abstract class DB : RoomDatabase() {

    abstract val agnaDAO: AgnaDAO
    abstract val dailyFormDAO: DailyFormDAO

}