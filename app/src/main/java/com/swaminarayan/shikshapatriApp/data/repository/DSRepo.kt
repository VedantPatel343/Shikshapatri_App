package com.swaminarayan.shikshapatriApp.data.repository

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.swaminarayan.shikshapatriApp.data.DataStore
import kotlinx.coroutines.flow.Flow

@RequiresApi(Build.VERSION_CODES.O)
class DSRepo(context: Context) {

    private val ds = DataStore(context)

    fun getHomeFirstDay(): Flow<String> {
        return ds.getHomeFirstDay()
    }

    fun getHomeLastDay(): Flow<String> {
        return ds.getHomeLastDay()
    }

    fun getSLogan(): Flow<String> {
        return ds.getSlogan()
    }

    suspend fun updateHomeFirstDay(date: String) {
        ds.updateHomeFirstDay(date)
    }

    suspend fun updateHomeLastDay(date: String) {
        ds.updateHomeLastDay(date)
    }

    suspend fun updateSlogan(slogan: String) {
        ds.updateSlogan(slogan)
    }

}