package com.swaminarayan.shikshapatriApp.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaminarayan.shikshapatriApp.data.repository.DSRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
//@HiltViewModel
class SplashViewModel @Inject constructor(
    private val dsRepo: DSRepo
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            checkLocalDateMonday()
            delay(700)
            _isLoading.value = false
        }
    }

    private fun checkLocalDateMonday() {
        val today = LocalDate.now()
        val mondayValue = DayOfWeek.MONDAY.value
        val todayWeekDayValue = today.dayOfWeek.value

        val balance = todayWeekDayValue - mondayValue
        val firstDay = today.minusDays(balance.toLong())
        val lastDay = firstDay.plusDays(6)

        viewModelScope.launch(Dispatchers.IO) {
            dsRepo.updateHomeFirstDay(firstDay.toString())
            dsRepo.updateHomeLastDay(lastDay.toString())
        }
    }

}