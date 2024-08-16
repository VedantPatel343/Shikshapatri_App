package com.example.shikshapatriapp.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Uppkar(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val uppkar: String,
    val date: LocalDate,
    val color: Int
)