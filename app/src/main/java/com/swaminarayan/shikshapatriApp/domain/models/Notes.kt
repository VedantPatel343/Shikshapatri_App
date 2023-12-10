package com.swaminarayan.shikshapatriApp.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Notes(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val des: String,
    val dateTime: LocalDateTime
)
