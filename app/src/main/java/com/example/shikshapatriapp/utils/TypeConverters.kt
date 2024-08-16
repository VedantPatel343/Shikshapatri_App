package com.example.shikshapatriapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
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