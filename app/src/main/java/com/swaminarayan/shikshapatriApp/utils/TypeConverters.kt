package com.swaminarayan.shikshapatriApp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.swaminarayan.shikshapatriApp.domain.models.DailyAgnas
import java.time.LocalDateTime

class LocalDateTimeConverter {
    @TypeConverter
    fun timeToString(time: LocalDateTime): String {
        return time.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun stringToTime(string: String): LocalDateTime {
        return LocalDateTime.parse(string)
    }
}

class DailyScoreListConverter {
    @TypeConverter
    fun listToJson(value: List<DailyAgnas>): String = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(string: String) = Gson().fromJson(string, Array<DailyAgnas>::class.java).toList()
}