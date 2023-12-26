package com.swaminarayan.shikshapatriApp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
fun dateFormatter(date: LocalDate): String {
    val pattern = "dd-MM-yyyy"
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return date.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun dayFormatter(date: LocalDate): String {
    val pattern = "EEE"
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return date.format(formatter)
}