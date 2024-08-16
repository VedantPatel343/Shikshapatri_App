package com.example.shikshapatriapp.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class DailyForm(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val date: LocalDate
)