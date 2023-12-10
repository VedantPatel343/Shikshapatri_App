package com.swaminarayan.shikshapatriApp.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.swaminarayan.shikshapatriApp.utils.DailyScoreListConverter
import com.swaminarayan.shikshapatriApp.utils.LocalDateTimeConverter
import com.swaminarayan.shikshapatriApp.data.data_source.AgnaDAO
import com.swaminarayan.shikshapatriApp.data.data_source.DailyReportDAO
import com.swaminarayan.shikshapatriApp.data.data_source.GoalsDAO
import com.swaminarayan.shikshapatriApp.domain.models.Agna
import com.swaminarayan.shikshapatriApp.domain.models.DailyAgnaFormReport
import com.swaminarayan.shikshapatriApp.domain.models.Goals
import com.swaminarayan.shikshapatriApp.domain.models.Notes

@Database(entities = [Agna::class, DailyAgnaFormReport::class, Goals::class, Notes::class], version = 1)
@TypeConverters(LocalDateTimeConverter::class, DailyScoreListConverter::class)
abstract class DB : RoomDatabase() {

    abstract val agnaDAO: AgnaDAO
    abstract val goalsDAO: GoalsDAO
    abstract val dailyReportDAO: DailyReportDAO

}