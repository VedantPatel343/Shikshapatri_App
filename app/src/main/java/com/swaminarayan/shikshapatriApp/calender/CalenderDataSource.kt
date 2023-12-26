package com.swaminarayan.shikshapatriApp.calender

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.stream.Collectors
import java.util.stream.Stream

@RequiresApi(Build.VERSION_CODES.O)
class CalenderDataSource {

    private val today: LocalDate = LocalDate.now()

    fun getVisibleDates(startDateTime: LocalDate = today): List<LocalDate> {
        val lastDayOfWeek = startDateTime.plusDays(7)
        return getDatesBetween(startDateTime.minusDays(1), lastDayOfWeek)
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