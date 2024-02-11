package com.swaminarayan.shikshapatriApp.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Agna(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val agna: String,
    val description: String,
    val author: String,
    val slokNo: Int,
    val rajipoPoints: Int,
    val alwaysPalayChe: Boolean
)
