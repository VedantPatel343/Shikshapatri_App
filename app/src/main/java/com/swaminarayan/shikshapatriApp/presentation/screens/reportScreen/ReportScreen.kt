package com.swaminarayan.shikshapatriApp.presentation.screens.reportScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowLeft
import androidx.compose.material.icons.filled.ArrowRight
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
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
//    val totalAgnaPoints = vm.totalPoints
    val totalAgnaPalaiPoints = vm.agnaPalaiPoints
    val totalAgnaNaPalaiPoints = vm.agnaNaPalaiPoints
    val currentMonth = vm.currentMonth.name

    val pieChartList = listOf(
        PieChartInput(Green, totalAgnaPalaiPoints, "Agna Palan"),
        PieChartInput(Red, totalAgnaNaPalaiPoints, "Agna Lop")
    )

    val reportAgnaItems = vm.reportAgnaItemList.toList()
    val monthlyForms = vm.monthlyForms

    Page(modifier = Modifier.padding(horizontal = 10.dp)) {

        LazyColumn {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                IconButton(
                    onClick = {
                        scope.launch { drawerState.open() }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "$currentMonth days:",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(15.dp))
                Calender(
                    startEndDate = "",
                    visibleDateList = monthlyForms,
                    onDateClicked = {
                        scope.launch {
                            val id = vm.getIdByDate(it)
                            navController.navigate("single_day_report_screen/${id}")
                        }
                    },
                    onNextClick = { },
                    onPreviousClick = { },
                    showPrevNextBtn = false
                )
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { vm.onPreviousMonthClicked() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowLeft,
                            contentDescription = "Left arrow key"
                        )
                    }
                    Text(
                        text = currentMonth,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = { vm.onNextMonthClicked() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowRight,
                            contentDescription = "Right arrow key"
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                PieChart(
                    pieChartList
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

            items(reportAgnaItems, key = { it.agnaId }) {
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