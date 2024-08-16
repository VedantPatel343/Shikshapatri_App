package com.example.shikshapatriapp.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DailyAgna(
    @PrimaryKey(autoGenerate = true)
    val dailyAgnaId: Long = 0L,
    val agnaId: Long,
    val dailyFormId: Long,
    val palai: Boolean? = null,
    val count: Int,
    val note: String
)

data class DailyAgnaHelperClass(
    val dailyAgnaId: Long? = null,
    val agnaId: Long,
    val palai: Boolean?,
    val agna: String,
    val description: String,
    val author: String,
    val rajipoPoints: Int,
    val alwaysPalayChe: Boolean,
    val isCounter: Boolean,
    val count: Int,
    val note: String
)