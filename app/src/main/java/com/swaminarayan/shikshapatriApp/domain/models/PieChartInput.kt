package com.swaminarayan.shikshapatriApp.domain.models

import androidx.compose.ui.graphics.Color

data class PieChartInput(
    val color: Color,
    val value: Int,
    val des: String = ""
)
