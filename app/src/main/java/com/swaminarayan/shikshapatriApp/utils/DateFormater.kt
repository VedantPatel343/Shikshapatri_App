package com.swaminarayan.shikshapatriApp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
fun dateFormatter(date: LocalDateTime): String {
    val pattern = "dd-MM-yyyy"
    val formatter = DateTimeFormatter.ofPattern(pattern)
    return date.format(formatter)
}