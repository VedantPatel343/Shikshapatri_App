package com.swaminarayan.shikshapatriApp.calender

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors
import java.util.stream.Stream

@RequiresApi(Build.VERSION_CODES.O)
class CalenderDataSource {

    fun getVisibleDates(startDate: LocalDate): List<LocalDate> {
        val lastDayOfWeek = startDate.plusDays(7)
        return getDatesBetween(startDate, lastDayOfWeek)
    }

    private fun getDatesBetween(
        startDate: LocalDate,
        endDate: LocalDate
    ): List<LocalDate> {
        val numOfDays = ChronoUnit.DAYS.between(startDate, endDate)
        return Stream.iterate(startDate) { date ->
            date.plusDays(1)
        }.limit(numOfDays)
            .collect(Collectors.toList())
    }


}