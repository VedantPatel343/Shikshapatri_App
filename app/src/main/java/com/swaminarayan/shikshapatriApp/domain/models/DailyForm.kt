package com.swaminarayan.shikshapatriApp.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class DailyForm(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val dailyAgnas: List<DailyAgna>,
    val date: LocalDate
)

data class DailyAgna(
    val id: Long = 0L,
    val palai: Boolean?
)

data class DailyAgnaHelperClass(
    val id: Long = 0L,
    val palai: Boolean?,
    val agna: String,
    val description: String,
    val author: String,
    val slokNo: Int,
    val rajipoPoints: Int,
    val alwaysPalayChe: Boolean
)