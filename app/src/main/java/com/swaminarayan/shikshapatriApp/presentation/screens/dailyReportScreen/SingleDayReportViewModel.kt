package com.swaminarayan.shikshapatriApp.presentation.screens.dailyReportScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaminarayan.shikshapatriApp.constants.FORM_ID
import com.swaminarayan.shikshapatriApp.data.repository.AgnaRepo
import com.swaminarayan.shikshapatriApp.data.repository.DailyFormRepo
import com.swaminarayan.shikshapatriApp.domain.models.DailyAgna
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class SingleDayReportViewModel @Inject constructor(
    private val dailyFormRepo: DailyFormRepo,
    agnaRepo: AgnaRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var totalAgnas = 0
    var date: LocalDate = LocalDate.now()
    var agnaPalaiList : MutableList<DailyAgna> = mutableListOf()
    var agnaNaPalaiList : MutableList<DailyAgna> = mutableListOf()
    var totalAgnaPalaiPoints = 0
    var totalAgnaNaPalaiPoints = 0


    init {
        savedStateHandle.get<Long>(FORM_ID)?.let {
            viewModelScope.launch {
                totalAgnas = agnaRepo.agnas().size
                setUpList(it)
            }
        }

    }

    private fun setUpList(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val form = dailyFormRepo.getDailyFormById(id)
            date = form.date
            form.dailyAgnas.forEach { agna ->
                if (agna.palai == true) {
                    totalAgnaPalaiPoints += agna.points
                    agnaPalaiList.add(agna)
                } else {
                    totalAgnaNaPalaiPoints += agna.points
                    agnaNaPalaiList.add(agna)
                }
            }
        }
    }

}