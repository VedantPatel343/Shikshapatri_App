package com.swaminarayan.shikshapatriApp.presentation.screens.homeScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.swaminarayan.shikshapatriApp.data.repository.DailyFormRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel @Inject constructor(
    private val dailyFormRepo: DailyFormRepo
) : ViewModel() {

    suspend fun getIdByDate(date: LocalDate): Long {
        return withContext(Dispatchers.IO) {
            val form = dailyFormRepo.getIdByDate(date)
            form?.id ?: -1L
        }
    }

}