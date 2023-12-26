package com.swaminarayan.shikshapatriApp.presentation.screens.dailyReportScreen

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.swaminarayan.shikshapatriApp.domain.models.PieChartInput
import com.swaminarayan.shikshapatriApp.presentation.components.Page
import com.swaminarayan.shikshapatriApp.presentation.components.PieChart
import com.swaminarayan.shikshapatriApp.ui.theme.Green
import com.swaminarayan.shikshapatriApp.ui.theme.Red
import com.swaminarayan.shikshapatriApp.utils.dateFormatter
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SingleDayReportScreen(
    navController: NavHostController,
    vm: SingleDayReportViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()

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

    Page {

        LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)) {
            item {
                Spacer(modifier = Modifier.height(10.dp))
                IconButton(
                    onClick = {
                        scope.launch { navController.popBackStack() }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

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
                    list
                )
                Spacer(modifier = Modifier.height(5.dp))
            }

            item {
                Column(modifier = Modifier.padding(bottom = 30.dp)) {
                    Text(
                        text = "$totalAgnas Total Agnas",
                        modifier = Modifier
                            .fillMaxWidth(),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
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

            items(agnasPalaiList, key = { it.id }) {
                Text(text = it.agna)
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                        .padding(top = 10.dp),
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

            items(agnasNaPalaiList, key = { it.id }) {
                Text(text = it.agna)
            }

        }
    }
}
