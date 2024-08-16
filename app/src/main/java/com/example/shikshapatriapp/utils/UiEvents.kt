package com.example.shikshapatriapp.utils

sealed class UiEvents {
    data class ShowToast(val message: String, val isLenShort: Boolean = true) : UiEvents()
}