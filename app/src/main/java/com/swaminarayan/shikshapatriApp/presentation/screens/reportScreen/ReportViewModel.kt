package com.swaminarayan.shikshapatriApp.presentation.screens.reportScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swaminarayan.shikshapatriApp.data.repository.AgnaRepo
import com.swaminarayan.shikshapatriApp.data.repository.DailyFormRepo
import com.swaminarayan.shikshapatriApp.domain.models.Agna
import com.swaminarayan.shikshapatriApp.domain.models.DailyForm
import com.swaminarayan.shikshapatriApp.domain.models.ReportAgnaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.Month
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class ReportViewModel @Inject constructor(
    private val dailyFormRepo: DailyFormRepo,
    private val agnaRepo: AgnaRepo
) : ViewModel() {

    var totalAgnas = 0

    private val _agnaPalaiPoints: MutableStateFlow<Long> = MutableStateFlow(0)
    val agnaPalaiPoints = _agnaPalaiPoints.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )

    private val _agnaNaPalaiPoints: MutableStateFlow<Long> = MutableStateFlow(0)
    val agnaNaPalaiPoints = _agnaNaPalaiPoints.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        0
    )

    private var dailyFormList = emptyList<DailyForm>()

    private val _currentMonth: MutableStateFlow<Month> = MutableStateFlow(LocalDate.now().month)
    val currentMonth = _currentMonth.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        LocalDate.now().month
    )

    private val today = LocalDate.now()
    private val _date15: MutableStateFlow<LocalDate> =
        MutableStateFlow(LocalDate.of(today.year, today.month, 15))
    val date15 = _date15.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        LocalDate.of(today.year, today.month, 15)
    )

    private val _monthlyForms: MutableStateFlow<MutableList<LocalDate>> =
        MutableStateFlow(mutableListOf())
    val monthlyForms = _monthlyForms.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        mutableListOf()
    )

    private val _reportAgnaItemList: MutableStateFlow<MutableList<ReportAgnaItem>> =
        MutableStateFlow(mutableListOf())
    val reportAgnaItemList = _reportAgnaItemList.map { it }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        mutableListOf()
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            totalAgnas = agnaRepo.agnas().size
            dailyFormList = dailyFormRepo.dailyFormList()

            setUpList(dailyFormList)
        }
    }

    private fun setUpList(dailyForms: List<DailyForm>) {
        viewModelScope.launch(Dispatchers.IO) {
            _monthlyForms.value.clear()
            _reportAgnaItemList.value.clear()
            _agnaPalaiPoints.value = 0L
            _agnaNaPalaiPoints.value = 0L

            dailyForms.forEach { form ->
                if (form.date.month == _currentMonth.value && form.date.year == _date15.value.year) {
                    _monthlyForms.value.add(form.date)
                    form.dailyAgnas.forEach {
                        val agna = agnaRepo.getAgnaById(it.id)
                        try {
                            if (agna != null) {
                                setReportItemModelList(agna, it.palai)
                                if (it.palai == true)
                                    _agnaPalaiPoints.value += agna.rajipoPoints
                                else
                                    _agnaNaPalaiPoints.value += agna.rajipoPoints
                            }
                        } catch (e: Exception) {
                            Log.i("exceptionCaught", "Report VM: $e")
                        }
                    }
                }
            }

        }
    }

    private fun setReportItemModelList(agna: Agna, palai: Boolean?) {
        val item = _reportAgnaItemList.value.find { it.agnaId == agna.id }
        _reportAgnaItemList.value.removeAll { it.agnaId == agna.id }

        if (item != null) {
            val reportItem = ReportAgnaItem(
                agnaId = item.agnaId,
                agna = item.agna,
                totalAgnaPoints = item.totalAgnaPoints + agna.rajipoPoints.toLong(),
                agnaPalaiPoints = if (palai == true)
                    item.agnaPalaiPoints + agna.rajipoPoints.toLong()
                else
                    item.agnaPalaiPoints
            )
            _reportAgnaItemList.value.add(reportItem)
        } else {
            val reportItem = ReportAgnaItem(
                agnaId = agna.id,
                agna = agna.agna,
                totalAgnaPoints = agna.rajipoPoints.toLong(),
                agnaPalaiPoints = if (palai == true) agna.rajipoPoints.toLong() else 0L
            )
            _reportAgnaItemList.value.add(reportItem)
        }
    }

    fun onNextMonthClicked() {
        update15Date(true)
        _currentMonth.value = _currentMonth.value.plus(1)
        setUpList(dailyFormList)
    }

    private fun update15Date(onNextClicked: Boolean) {
        val queryDate =
            if (onNextClicked) _date15.value.plusDays(30) else _date15.value.minusDays(30)
        val balance = if (queryDate > _date15.value) {
            queryDate.dayOfMonth - _date15.value.dayOfMonth
        } else {
            _date15.value.dayOfMonth - queryDate.dayOfMonth
        }

        _date15.value = if (queryDate > _date15.value) {
            queryDate.minusDays(balance.toLong())
        } else {
            queryDate.plusDays(balance.toLong())
        }
    }

    fun onPreviousMonthClicked() {
        update15Date(false)
        _currentMonth.value = _currentMonth.value.minus(1)
        setUpList(dailyFormList)
    }

    suspend fun getIdByDate(date: LocalDate): Long {
        return withContext(Dispatchers.IO) {
            dailyFormRepo.getIdByDate(date)?.id!!
        }
    }

}