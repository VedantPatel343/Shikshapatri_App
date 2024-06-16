package com.swaminarayan.shikshapatriApp.presentation.screens.reportScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        PieChartInput(Red, state.agnaLopPoints, "Agna Lop"),
        PieChartInput(Color.Gray, state.remainingAgnaPoints, "Remaining Agna")
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
                    currentYear = state.date15.year.toString()
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
                ReportAgnaItem(
                    reportAgnaItem = it
                )
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReportAgnaItem(reportAgnaItem: ReportAgnaItem) {

    val agnaPalanPoints = reportAgnaItem.agnaPalanPoints
    val agnaLopPoints = reportAgnaItem.agnaLopPoints
    val remainingAgnaPoints = reportAgnaItem.remainingAgnaPoints
    var isDescriptionVisible by remember {
        mutableStateOf(false)
    }

    val pieChartList = listOf(
        PieChartInput(Green, agnaPalanPoints, "Agna Palan"),
        PieChartInput(Red, agnaLopPoints, "Agna Lop"),
        PieChartInput(Color.Gray, remainingAgnaPoints, "Remaining Agna")
    )

    Card(
        elevation = CardDefaults.cardElevation(3.dp),
        modifier = Modifier.padding(bottom = 10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 5.dp, start = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(0.85f),
                    text = reportAgnaItem.agna,
                    fontSize = 18.sp
                )
                AnimatedVisibility(
                    !isDescriptionVisible,
                    exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Top),
                    enter = fadeIn() + expandVertically(expandFrom = Alignment.Top)
                ) {
                    SmallPieChart(pieChartList)
                }
                IconButton(onClick = { isDescriptionVisible = !isDescriptionVisible }) {
                    Icon(
                        imageVector = if (!isDescriptionVisible) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }

            AnimatedVisibility(visible = isDescriptionVisible) {
                ShowDescription(
                    pieChartList = pieChartList,
                    reportAgnaItem = reportAgnaItem
                )
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShowDescription(
    modifier: Modifier = Modifier,
    pieChartList: List<PieChartInput>,
    reportAgnaItem: ReportAgnaItem
) {

    val agnaPalanPercentage =
        (reportAgnaItem.agnaPalanPoints / reportAgnaItem.totalPoints.toFloat() * 100).toInt()
    val agnaLopPercentage =
        (reportAgnaItem.agnaLopPoints / reportAgnaItem.totalPoints.toFloat() * 100).toInt()
    val remainingAgnaPercentage =
        (reportAgnaItem.remainingAgnaPoints / reportAgnaItem.totalPoints.toFloat() * 100).toInt()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SmallPieChart(pieChartList)
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Agna Palai - $agnaPalanPercentage%", color = Green, fontSize = 18.sp)
            Text(text = "Agna Lopai - $agnaLopPercentage%", color = Red, fontSize = 18.sp)
        }
        Text(
            text = "Remaining Agnas - $remainingAgnaPercentage%",
            color = Color.Gray,
            fontSize = 18.sp
        )

        if (reportAgnaItem.noteList.isNotEmpty()) {

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Notes:",
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(5.dp))

            reportAgnaItem.noteList.forEachIndexed { index, pair ->
                Column {
                    HorizontalDivider()
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 10.dp)
                    ) {
                        Spacer(modifier = Modifier.height(5.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${index + 1}. ",
                                fontSize = 18.sp,
                            )
                            Text(
                                text = pair.first,
                                fontSize = 18.sp,
                            )
                        }
                        Text(
                            text = "Date - ${pair.second.dayOfMonth}/${pair.second.monthValue}",
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        } else {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "No notes added.",
                fontSize = 18.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

    }
}