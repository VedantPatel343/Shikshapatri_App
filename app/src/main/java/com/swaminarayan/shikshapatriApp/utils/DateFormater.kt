package com.swaminarayan.shikshapatriApp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toFormattedDate() : String {
    val pattern = "dd-MM-yyyy"
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toFormattedDay() : String {
    val pattern = "EEE"
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return this.format(formatter)
}