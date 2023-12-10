package com.swaminarayan.shikshapatriApp.utils

sealed class UiEvents {
    data class ShowToast(val message: String, val isLenShort: Boolean = true) : UiEvents()
}