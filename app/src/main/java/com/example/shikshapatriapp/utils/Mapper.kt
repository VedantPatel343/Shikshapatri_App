package com.swaminarayan.shikshapatriApp.utils

import com.example.shikshapatriapp.domain.models.Agna
import com.example.shikshapatriapp.domain.models.DailyAgna
import com.example.shikshapatriapp.domain.models.DailyAgnaHelperClass
import com.example.shikshapatriapp.presentation.screens.aeAgnaScreen.AEAgnaViewModel

fun AEAgnaViewModel.AEAgnaUiState.toAgna(agnaId: Long?): Agna =
    if (agnaId == null) {
        Agna(
            agna = this.agna,
            description = this.des,
            author = this.author,
            rajipoPoints = if (this.rajipoPoints.isEmpty()) 0 else this.rajipoPoints.toInt(),
            alwaysPalayChe = this.alwaysPalayChe,
            isCounter = this.isCounter
        )
    } else {
        Agna(
            id = agnaId,
            agna = this.agna,
            description = this.des,
            author = this.author,
            rajipoPoints = if (this.rajipoPoints.isEmpty()) 0 else this.rajipoPoints.toInt(),
            alwaysPalayChe = this.alwaysPalayChe,
            isCounter = this.isCounter
        )
    }

fun Agna.toDailyAgnaHelperClass(palai: Boolean?, count: Int, note: String, dailyAgnaId: Long?): DailyAgnaHelperClass =
    DailyAgnaHelperClass(
        dailyAgnaId = dailyAgnaId,
        agnaId = this.id,
        agna = this.agna,
        description = this.description,
        rajipoPoints = this.rajipoPoints,
        author = this.author,
        alwaysPalayChe = this.alwaysPalayChe,
        palai = palai,
        isCounter = this.isCounter,
        count = count,
        note = note
    )

fun List<DailyAgnaHelperClass>.toListOfDailyAgna(dailyFormId: Long, isFormAlreadySaved: Boolean): List<DailyAgna> =
    this.map {
        if (isFormAlreadySaved) {
            DailyAgna(
                dailyAgnaId = it.dailyAgnaId ?: -1L,
                agnaId = it.agnaId,
                dailyFormId = dailyFormId,
                palai = it.palai,
                count = it.count,
                note = it.note
            )
        } else {
            DailyAgna(
                agnaId = it.agnaId,
                dailyFormId = dailyFormId,
                palai = it.palai,
                count = it.count,
                note = it.note
            )
        }
    }
