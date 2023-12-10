package com.swaminarayan.shikshapatriApp.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Goals(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val goal: String,
    val isAchieved: Boolean,
    val dateTime: LocalDateTime
)
