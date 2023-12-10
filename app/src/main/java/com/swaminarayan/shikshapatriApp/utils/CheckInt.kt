package com.swaminarayan.shikshapatriApp.utils

fun checkInt(it: String): Boolean {
    val int = 0..Int.MAX_VALUE
    try {
        if (int.contains(it.toInt())) {
            return true
        }
    } catch (e: Exception) {
        return false
    }
    return false
}

