package com.swaminarayan.shikshapatriApp.utils

import com.swaminarayan.shikshapatriApp.domain.models.Agna
import com.swaminarayan.shikshapatriApp.domain.models.DailyAgna
import com.swaminarayan.shikshapatriApp.domain.models.DailyAgnaHelperClass
import com.swaminarayan.shikshapatriApp.presentation.screens.aeAgnaScreen.AEAgnaViewModel

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

fun Agna.toDailyAgnaHelperClass(palai: Boolean?, count: Int, note: String): DailyAgnaHelperClass =
    DailyAgnaHelperClass(
        id = this.id,
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

fun List<DailyAgnaHelperClass>.toListOfDailyAgna(): List<DailyAgna> =
    this.map {
        DailyAgna(
            id = it.id,
            palai = it.palai,
            count = it.count,
            note = it.note
        )
    }
