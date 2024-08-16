package com.example.shikshapatriapp.domain.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.shikshapatriapp.domain.models.DailyAgna
import com.example.shikshapatriapp.domain.models.DailyForm

data class DailyFormWithDailyAgna(
    @Embedded
    val dailyForm: DailyForm,
    @Relation(
        parentColumn = "id",
        entityColumn = "dailyFormId"
    )
    val dailyAgnas: List<DailyAgna>
)