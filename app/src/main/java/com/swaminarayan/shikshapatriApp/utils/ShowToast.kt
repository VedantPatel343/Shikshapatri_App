package com.swaminarayan.shikshapatriApp.utils

import android.content.Context
import android.widget.Toast

fun showToast(context: Context, message: String, isLenShort: Boolean) {
    val duration = if (isLenShort) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
    Toast.makeText(context, message, duration).show()
}