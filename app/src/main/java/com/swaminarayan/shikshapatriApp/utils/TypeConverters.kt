package com.swaminarayan.shikshapatriApp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.swaminarayan.shikshapatriApp.domain.models.DailyAgna
import java.time.LocalDate

class LocalDateConverter {
    @TypeConverter
    fun timeToString(time: LocalDate): String {
        return time.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun stringToTime(string: String): LocalDate {
        return LocalDate.parse(string)
    }
}

class DailyAgnaListConverter {
    @TypeConverter
    fun listToJson(value: List<DailyAgna>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun jsonToList(string: String) : List<DailyAgna> {
        return Gson().fromJson(string, Array<DailyAgna>::class.java).toList()
    }
}