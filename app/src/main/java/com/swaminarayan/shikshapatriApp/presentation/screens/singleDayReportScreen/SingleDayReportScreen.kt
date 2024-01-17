package com.swaminarayan.shikshapatriApp.presentation.screens.singleDayReportScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.swaminarayan.shikshapatriApp.domain.models.PieChartInput
import com.swaminarayan.shikshapatriApp.presentation.components.Page
import com.swaminarayan.shikshapatriApp.presentation.components.PieChart
import com.swaminarayan.shikshapatriApp.ui.theme.Green
import com.swaminarayan.shikshapatriApp.ui.theme.Red
import com.swaminarayan.shikshapatriApp.utils.dateFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SingleDayReportScreen(
    navController: NavHostController,
    vm: SingleDayReportViewModel = hiltViewModel()
) {

    val date = vm.date
    val totalAgnas = vm.totalAgnas
    val totalAgnaPalaiPoints = vm.totalAgnaPalaiPoints
    val totalAgnaNaPalaiPoints = vm.totalAgnaNaPalaiPoints
    val list = listOf(
        PieChartInput(Green, totalAgnaPalaiPoints, "Agna Palan"),
        PieChartInput(Red, totalAgnaNaPalaiPoints, "Agna Lop")
    )
    val agnasNaPalaiList = vm.agnaNaPalaiList.toList()
    val agnasPalaiList = vm.agnaPalaiList.toList()
    val remainingAgnas = vm.remainingAgna

    Page {
        Spacer(modifier = Modifier.height(10.dp))
        IconButton(
            onClick = {
                navController.popBackStack()
            },
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)) {
            item {
                Text(
                    text = "${dateFormatter(date)}\n${date.dayOfWeek}",
                    modifier = Modifier
                        .fillMaxWidth(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(30.dp))
                PieChart(
                    list,
                    showArrowBtn = false,
                    onPreviousMonthClicked = {},
                    onNextMonthClicked = {},
                    date15year = "",
                    currentMonth = ""
                )
                Spacer(modifier = Modifier.height(5.dp))
            }

            item {
                Column(modifier = Modifier.padding(bottom = 30.dp)) {

                    Text(
                        text = "$totalAgnas - Total Agnas",
                        modifier = Modifier
                            .fillMaxWidth(),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    if (remainingAgnas != 0L) {
                        Text(
                            text = "$remainingAgnas - Remaining Agnas to fill.",
                            modifier = Modifier
                                .fillMaxWidth(),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(if (remainingAgnas != 0L) 30.dp else 10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${agnasPalaiList.size} - Agna Palai",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Green
                        )

                        Text(
                            text = "${agnasNaPalaiList.size} - Agna Na Palai",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Red
                        )
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Green,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .size(45.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "${agnasPalaiList.size} / $totalAgnas Agna Palai",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            itemsIndexed(
                agnasPalaiList,
                key = { _, item ->
                    item.id
                }
            ) { index, item ->
                Row(
                    modifier = Modifier.padding(bottom = 5.dp, start = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${index + 1}.",
                        fontSize = 18.sp,
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = item.agna,
                        fontSize = 18.sp,
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .padding(top = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Red,
                                shape = RoundedCornerShape(10.dp)
                            )
                            .size(45.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "${agnasNaPalaiList.size} / $totalAgnas Agna Na Palai",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            itemsIndexed(
                agnasNaPalaiList,
                key = { _, item ->
                    item.id
                }
            ) { index, item ->
                Row(
                    modifier = Modifier.padding(bottom = 5.dp, start = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${index + 1}.",
                        fontSize = 18.sp,
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = item.agna,
                        fontSize = 18.sp,
                    )
                }
            }

        }
    }
}
