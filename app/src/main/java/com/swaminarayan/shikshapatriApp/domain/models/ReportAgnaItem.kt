package com.swaminarayan.shikshapatriApp.domain.models

data class ReportAgnaItem(
    val agnaId: Long,
    val agna: String,
    val totalPoints: Long,
    val agnaPalanPoints: Long,
    val agnaLopPoints: Long
)
