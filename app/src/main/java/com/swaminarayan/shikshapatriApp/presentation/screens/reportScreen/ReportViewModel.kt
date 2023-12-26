package com.swaminarayan.shikshapatriApp.presentation.screens.reportScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaminarayan.shikshapatriApp.data.repository.AgnaRepo
import com.swaminarayan.shikshapatriApp.data.repository.DailyFormRepo
import com.swaminarayan.shikshapatriApp.domain.models.DailyAgna
import com.swaminarayan.shikshapatriApp.domain.models.DailyForm
import com.swaminarayan.shikshapatriApp.domain.models.ReportAgnaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.Month
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ReportViewModel @Inject constructor(
    private val dailyFormRepo: DailyFormRepo,
    agnaRepo: AgnaRepo
) : ViewModel() {

    //        var totalPoints = 0
    var agnaPalaiPoints = 0
    var agnaNaPalaiPoints = 0
    var monthlyForms: MutableList<LocalDate> = mutableListOf()

    var totalAgnas = 0
    var currentMonth: Month = LocalDate.now().month

    var reportAgnaItemList: MutableList<ReportAgnaItem> = mutableListOf()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            totalAgnas = agnaRepo.agnas().size

            setUpList(dailyFormRepo.dailyFormList())
        }
    }

    private fun setUpList(dailyForms: List<DailyForm>) {
        viewModelScope.launch(Dispatchers.IO) {

            dailyForms.forEach { form ->
                if (form.date.month == currentMonth) {
                    monthlyForms.add(form.date)
//                totalPoints += form.dailyAgnas.sumOf { it.points }
                    form.dailyAgnas.forEach {
                        setReportItemModelList(it)
                        if (it.palai == true)
                            agnaPalaiPoints += it.points
                        else
                            agnaNaPalaiPoints += it.points
                    }
                }
            }

        }
    }

    private fun setReportItemModelList(dailyAgna: DailyAgna) {
        viewModelScope.launch(Dispatchers.IO) {
            val item = reportAgnaItemList.find { it.agnaId == dailyAgna.id }
            reportAgnaItemList.removeAll { it.agnaId == dailyAgna.id }

            if (item != null) {
                val reportItem = ReportAgnaItem(
                    agnaId = item.agnaId,
                    agna = item.agna,
                    totalAgnaPoints = item.totalAgnaPoints + dailyAgna.points,
                    agnaPalaiPoints = if (dailyAgna.palai == true)
                        item.agnaPalaiPoints + dailyAgna.points
                    else
                        item.agnaPalaiPoints
                )
                reportAgnaItemList.add(reportItem)
            } else {
                val reportItem = ReportAgnaItem(
                    agnaId = dailyAgna.id,
                    agna = dailyAgna.agna,
                    totalAgnaPoints = dailyAgna.points,
                    agnaPalaiPoints = if (dailyAgna.palai == true) dailyAgna.points else 0
                )
                reportAgnaItemList.add(reportItem)
            }
        }
    }

    fun onNextMonthClicked() {
        // first make reportItemList empty then process further
        // change the currentMonth
    }

    fun onPreviousMonthClicked() {
        // first make reportItemList empty then process further
        // change the currentMonth
    }

    suspend fun getIdByDate(date: LocalDate): Long {
        return withContext(Dispatchers.IO) {
            dailyFormRepo.getIdByDate(date)?.id!!
        }
    }

}