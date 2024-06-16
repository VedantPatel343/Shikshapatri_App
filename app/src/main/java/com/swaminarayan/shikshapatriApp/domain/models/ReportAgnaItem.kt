package com.swaminarayan.shikshapatriApp.domain.models

import java.time.LocalDate
import java.util.Date

data class ReportAgnaItem(
    val agnaId: Long,
    val agna: String,
    val totalPoints: Long,
    val agnaPalanPoints: Long,
    val agnaLopPoints: Long,
    val remainingAgnaPoints: Long,
    val noteList: List<Pair<String, LocalDate>>
)
