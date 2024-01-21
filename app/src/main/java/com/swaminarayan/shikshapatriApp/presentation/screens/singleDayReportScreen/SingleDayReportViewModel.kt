package com.swaminarayan.shikshapatriApp.presentation.screens.singleDayReportScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaminarayan.shikshapatriApp.constants.FORM_ID
import com.swaminarayan.shikshapatriApp.data.repository.AgnaRepo
import com.swaminarayan.shikshapatriApp.data.repository.DailyFormRepo
import com.swaminarayan.shikshapatriApp.domain.models.Agna
import com.swaminarayan.shikshapatriApp.domain.models.DailyAgna
import com.swaminarayan.shikshapatriApp.domain.models.DailyForm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
@RequiresApi(Build.VERSION_CODES.O)
class SingleDayReportViewModel @Inject constructor(
    private val dailyFormRepo: DailyFormRepo,
    private val agnaRepo: AgnaRepo,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var totalAgnas = 0L
    var date: LocalDate = LocalDate.now()
    var agnaPalaiList : MutableList<Agna> = mutableListOf()
    var agnaNaPalaiList : MutableList<Agna> = mutableListOf()
    var totalAgnaPalaiPoints = 0L
    var totalAgnaNaPalaiPoints = 0L

    var remainingAgna = 0L

    init {
        savedStateHandle.get<Long>(FORM_ID)?.let {
            viewModelScope.launch {
                totalAgnas = agnaRepo.agnas().size.toLong()
                setUpList(it)
            }
        }
    }

    private fun setUpList(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val form = dailyFormRepo.getDailyFormById(id)
            date = form.date

            form.dailyAgnas.forEach { dailyAgna ->
                val agna = agnaRepo.getAgnaById(dailyAgna.id)
                try {
                    if (agna != null) {
                        if (dailyAgna.palai == true) {
                            totalAgnaPalaiPoints += agna.rajipoPoints
                            agnaPalaiList.add(agna)
                        } else {
                            totalAgnaNaPalaiPoints += agna.rajipoPoints
                            agnaNaPalaiList.add(agna)
                        }
                    } else {
                        removeAgnaFromDailyForm(form, dailyAgna, date)
                    }
                } catch (e: Exception) {
                    Log.i("exceptionCaught", "SingleDayReport VM: $e")
                }
            }
            remainingAgna = totalAgnas - (agnaPalaiList.size + agnaNaPalaiList.size)
        }
    }

    private fun removeAgnaFromDailyForm(
        form: DailyForm,
        dailyAgna: DailyAgna,
        date: LocalDate
    ) {
        val list = mutableListOf<DailyAgna>()
        form.dailyAgnas.forEach {
            if (it.id != dailyAgna.id) {
                list.add(dailyAgna)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            dailyFormRepo.upsertDailyForm(form.copy(dailyAgnas = list))
        }
    }

}