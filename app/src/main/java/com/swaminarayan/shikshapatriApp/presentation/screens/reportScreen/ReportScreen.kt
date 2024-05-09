package com.swaminarayan.shikshapatriApp.presentation.screens.reportScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.swaminarayan.shikshapatriApp.calender.ui.Calender
import com.swaminarayan.shikshapatriApp.domain.models.PieChartInput
import com.swaminarayan.shikshapatriApp.domain.models.ReportAgnaItem
import com.swaminarayan.shikshapatriApp.presentation.components.Page
import com.swaminarayan.shikshapatriApp.presentation.components.PieChart
import com.swaminarayan.shikshapatriApp.presentation.components.SmallPieChart
import com.swaminarayan.shikshapatriApp.ui.theme.Green
import com.swaminarayan.shikshapatriApp.ui.theme.Red
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReportScreen(
    navController: NavHostController,
    vm: ReportViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()
    val state by vm.state.collectAsStateWithLifecycle()

    val pieChartList = listOf(
        PieChartInput(Green, state.agnaPalanPoints, "Agna Palan"),
        PieChartInput(Red, state.agnaLopPoints, "Agna Lop")
    )

    Page(modifier = Modifier.padding(horizontal = 10.dp)) {

        Box(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .padding(top = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Report",
                fontSize = 30.sp,
                fontFamily = FontFamily.Cursive,
                color = MaterialTheme.colorScheme.primary
            )
        }

        LazyColumn {
            item {
                Text(
                    text = "${state.currentMonth}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(15.dp))

                AnimatedVisibility(visible = state.monthlyForms.toList().isEmpty()) {
                    Text(
                        text = "No forms filled.",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 15.dp),
                        textAlign = TextAlign.Center
                    )
                }

                AnimatedVisibility(visible = state.monthlyForms.toList().isNotEmpty()) {
                    Calender(
                        visibleDateList = state.monthlyForms.toList(),
                        centerText = "",
                        onDateClicked = {
                            scope.launch {
                                val id = vm.getIdByDate(it)
                                navController.navigate("single_day_report_screen/${id}")
                            }
                        },
                        onNextClick = { },
                        onPreviousClick = { },
                        showPrevNextBtn = false,
                        showTickMarks = { false }
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))

                PieChart(
                    pieChartList,
                    showArrowBtn = true,
                    onPreviousMonthClicked = { vm.onPreviousMonthClicked() },
                    onNextMonthClicked = { vm.onNextMonthClicked() },
                    currentMonth = state.currentMonth.name,
                    date15year = state.date15.year.toString()
                )
            }

            item {
                Text(
                    text = "Total Agnas: ${state.totalAgnas}",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Individual Agna Report:",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            items(
                items = state.reportAgnaItemList.toList(),
                key = { it.agnaId }
            ) {
                ReportAgnaItem(it, true)
            }

        }
    }
}

@Composable
fun ReportAgnaItem(reportAgnaItem: ReportAgnaItem, isAgnaPalanItem: Boolean) {

    val totalAgnaPoint = reportAgnaItem.totalPoints
    val agnaPalanPoints = reportAgnaItem.agnaPalanPoints
    val agnaLopPoints = reportAgnaItem.agnaLopPoints
    val percentage = if (isAgnaPalanItem) {
        (agnaPalanPoints / totalAgnaPoint.toFloat() * 100).toInt()
    } else {
        (agnaLopPoints / totalAgnaPoint.toFloat() * 100).toInt()
    }

    val pieChartList = listOf(
        PieChartInput(Green, agnaPalanPoints, "Agna Palan"),
        PieChartInput(Red, agnaLopPoints, "Agna Lop")
    )

    Card(
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier.padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(0.9f),
                text = reportAgnaItem.agna,
                fontSize = 18.sp
            )
            SmallPieChart(pieChartList, percentage, isAgnaPalanItem)
        }
    }

}