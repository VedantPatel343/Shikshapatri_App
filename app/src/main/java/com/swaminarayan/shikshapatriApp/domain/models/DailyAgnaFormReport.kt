package com.swaminarayan.shikshapatriApp.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DailyAgnaFormReport(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val dailyAgnas: List<DailyAgnas>,
    val totalScore: Int,
    val dateTime: Long
)

//data class DailyAgnas(
//    val agnaId: Long,
//    val palai: Boolean
//)

data class DailyAgnas(
    val id: Long = 0L,
    val agna: String,
    val description: String,
    val author: String,
    val slokNo: Int,
    val points: Int,
    val alwaysPalayChe: Boolean,
    val isStared: Boolean,
    val palai: Boolean?
)