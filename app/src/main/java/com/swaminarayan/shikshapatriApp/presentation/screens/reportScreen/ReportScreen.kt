package com.swaminarayan.shikshapatriApp.presentation.screens.reportScreen

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    drawerState: DrawerState,
    navController: NavHostController,
    vm: ReportViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()
    val totalAgnas = vm.totalAgnas
    val totalAgnaPalaiPoints by vm.agnaPalaiPoints.collectAsStateWithLifecycle()
    val totalAgnaNaPalaiPoints by vm.agnaNaPalaiPoints.collectAsStateWithLifecycle()
    val currentMonth by vm.currentMonth.collectAsStateWithLifecycle()
    val date15 by vm.date15.collectAsStateWithLifecycle()

    val pieChartList = listOf(
        PieChartInput(Green, totalAgnaPalaiPoints, "Agna Palan"),
        PieChartInput(Red, totalAgnaNaPalaiPoints, "Agna Lop")
    )

    val reportAgnaItems by vm.reportAgnaItemList.collectAsStateWithLifecycle()
    val monthlyForms by vm.monthlyForms.collectAsStateWithLifecycle()

    Log.i("listTest", "ReportScreen: $reportAgnaItems")
    Log.i("listTest", "ReportScreen: $monthlyForms")

    Page(modifier = Modifier.padding(horizontal = 10.dp)) {

        Spacer(modifier = Modifier.height(10.dp))
        IconButton(
            onClick = {
                scope.launch { drawerState.open() }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        LazyColumn {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "$currentMonth days:",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(15.dp))

                Log.i("TAG", "ReportScreen: ${monthlyForms.toList()}")

                AnimatedVisibility(visible = monthlyForms.toList().isEmpty()) {
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

                AnimatedVisibility(visible = monthlyForms.toList().isNotEmpty()) {
                    Calender(
                        visibleDateList = monthlyForms.toList(),
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
                    currentMonth = currentMonth.name,
                    date15year = date15.year.toString(),
                    onNextMonthClicked = { vm.onNextMonthClicked() },
                    onPreviousMonthClicked = { vm.onPreviousMonthClicked() }
                )
            }

            item {
                Text(
                    text = "Total Agnas: $totalAgnas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Agna Palai:",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            items(
                items = reportAgnaItems.toList(),
                key = { it.agnaId }
            ) {
                ReportAgnaItem(it)
            }

        }
    }
}

@Composable
fun ReportAgnaItem(reportAgnaItem: ReportAgnaItem) {

    val totalAgnaPoint = reportAgnaItem.totalAgnaPoints
    val agnaPalaiPoints = reportAgnaItem.agnaPalaiPoints
    val agnaNaPalaiPoints = totalAgnaPoint - agnaPalaiPoints
    val percentage = (agnaPalaiPoints / totalAgnaPoint.toFloat() * 100).toInt()

    val pieChartList = listOf(
        PieChartInput(Green, agnaPalaiPoints, "Agna Palan"),
        PieChartInput(Red, agnaNaPalaiPoints, "Agna Lop")
    )

    Card(
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier.padding(bottom = 10.dp)
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
            SmallPieChart(pieChartList, percentage)
        }
    }

}