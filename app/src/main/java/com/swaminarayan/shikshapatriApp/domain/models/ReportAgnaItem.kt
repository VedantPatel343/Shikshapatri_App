package com.swaminarayan.shikshapatriApp.domain.models

data class ReportAgnaItem(
    val agnaId: Long,
    val agna: String,
    val totalAgnaPoints: Int,
    val agnaPalaiPoints: Int
)
