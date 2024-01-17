package com.swaminarayan.shikshapatriApp.data

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
class DataStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("shikshapatri")
        val HOME_FIRST_DAY = stringPreferencesKey("home_first_day")
        val HOME_LAST_DAY = stringPreferencesKey("home_last_day")
        val SLOGAN = stringPreferencesKey("slogan")
    }

    fun getHomeFirstDay(): Flow<String> = context.dataStore.data.map {
        it[HOME_FIRST_DAY] ?: LocalDate.now().toString()
    }.flowOn(Dispatchers.IO)

    fun getHomeLastDay(): Flow<String> = context.dataStore.data.map {
        it[HOME_LAST_DAY] ?: LocalDate.now().plusDays(6).toString()
    }.flowOn(Dispatchers.IO)

    fun getSlogan(): Flow<String> = context.dataStore.data.map {
        it[SLOGAN] ?: "પૂ. ગુરૂજી અને મહારાજ ની અતી કૃપાને કારણે જ હું આ આજ્ઞાઓ નું પાલન કરી સકું છું."
    }.flowOn(Dispatchers.IO)

    suspend fun updateHomeFirstDay(date: String) {
        context.dataStore.edit {
            it[HOME_FIRST_DAY] = date
        }
    }

    suspend fun updateHomeLastDay(date: String) {
        context.dataStore.edit {
            it[HOME_LAST_DAY] = date
        }
    }

    suspend fun updateSlogan(slogan: String) {
        context.dataStore.edit {
            it[SLOGAN] = slogan
        }
    }

}