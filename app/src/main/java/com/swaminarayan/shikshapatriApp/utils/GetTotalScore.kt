package com.swaminarayan.shikshapatriApp.utils

import com.swaminarayan.shikshapatriApp.domain.models.DailyAgna

fun getTotalScore(list: List<DailyAgna>): Int {

    var totalScore = 0
    list.forEach {
        if (it.palai == true) {
            totalScore += it.points
        }
    }

    return totalScore
}