package com.swaminarayan.shikshapatriApp.presentation.screens.singleDayReportScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.swaminarayan.shikshapatriApp.R
import com.swaminarayan.shikshapatriApp.domain.models.PieChartInput
import com.swaminarayan.shikshapatriApp.presentation.components.Notice
import com.swaminarayan.shikshapatriApp.presentation.components.Page
import com.swaminarayan.shikshapatriApp.presentation.components.PieChart
import com.swaminarayan.shikshapatriApp.ui.theme.Green
import com.swaminarayan.shikshapatriApp.ui.theme.Red
import com.swaminarayan.shikshapatriApp.utils.toFormattedDate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SingleDayReportScreen(
    navController: NavHostController,
    vm: SingleDayReportViewModel = hiltViewModel()
) {

    val pullToRefreshState = rememberPullToRefreshState()
    val state by vm.state.collectAsStateWithLifecycle()
    val list = listOf(
        PieChartInput(Green, state.totalAgnaPalanPoints, "Agna Palan"),
        PieChartInput(Red, state.totalAgnaLopPoints, "Agna Lop"),
        PieChartInput(Color.Gray, state.totalRemainingAgnaPoints, "Agna Remaining")
    )
    var isNoticeVisible by rememberSaveable {
        mutableStateOf(false)
    }
    var isRefreshing by rememberSaveable {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    val blurValue = if (isRefreshing) 50.dp else 0.dp
    LaunchedEffect(key1 = true) {
        isNoticeVisible = true
        delay(5000)
        isNoticeVisible = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(pullToRefreshState.nestedScrollConnection),
        contentAlignment = Alignment.TopCenter
    ) {
        Page(modifier = Modifier.blur(blurValue)) {
            Notice(
                text = "Pull down to refresh.",
                isNoticeVisible = isNoticeVisible,
                leftArrowColor = Color.Transparent,
                isLeftIconVisible = false,
                isRightIconVisible = false
            )

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
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

                IconButton(
                    onClick = {
                        navController.navigate("daily_form_screen/${state.formId}/${state.date}")
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outlined_report_icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)) {
                item {
                    Text(
                        text = "${state.date.toFormattedDate()}\n${state.date.dayOfWeek}",
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
                        currentMonth = "",
                        currentYear = ""
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                }

                item {
                    Column {
                        Text(
                            text = "${state.totalAgnas} - Total Agnas",
                            modifier = Modifier
                                .fillMaxWidth(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.primary
                        )

                        if (state.newUnSavedAgnas != 0) {
                            Spacer(modifier = Modifier.height(7.dp))
                            Column {
                                Text(
                                    text = "${state.newUnSavedAgnas} - New/Unsaved Agnas",
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "(Please click on Daily Form button to fill the form)",
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(25.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(25.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (state.agnaPalanList.isNotEmpty()) {
                                Text(
                                    text = if (state.agnaPalanList.size != state.totalAgnas.toInt()) "${state.agnaPalanList.size} - Agnas Palai" else "All Agnas Palai",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Green
                                )
                            }

                            if (state.agnaLopList.isNotEmpty()) {
                                Text(
                                    text = if (state.agnaLopList.size != state.totalAgnas.toInt()) "${state.agnaLopList.size} - Agnas Lopai" else "All Agnas Lopai",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Red
                                )
                            }
                        }

                        if (state.remainingAgnaList.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (state.totalAgnas.toInt() != state.remainingAgnaList.size) "${state.remainingAgnaList.size} - Agnas Remaining" else "All Agnas are Remaining to fill",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = Color.Gray,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                        Spacer(modifier = Modifier.height(25.dp))
                    }
                }

                if (state.agnaPalanList.isNotEmpty()) {
                    item {
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(15.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
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
                                text = "${state.agnaPalanList.size} - Agna Palai",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                    itemsIndexed(
                        state.agnaPalanList,
                        key = { _, item ->
                            item.id
                        }
                    ) { index, item ->
                        Row(
                            modifier = Modifier.padding(bottom = 15.dp, start = 5.dp),
                        ) {
                            Text(
                                text = "${index + 1}.",
                                fontSize = 19.sp,
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Column {
                                Text(
                                    text = item.agna,
                                    fontSize = 19.sp,
                                )

                                if (item.note.isNotEmpty()) {
                                    Text(
                                        text = "Note = ${item.note}",
                                        fontSize = 16.sp,
                                    )
                                }
                            }
                        }
                    }
                }

                if (state.agnaLopList.isNotEmpty()) {
                    item {
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(15.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
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
                                text = "${state.agnaLopList.size} - Agna Na Palai",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                    itemsIndexed(
                        state.agnaLopList,
                        key = { _, item ->
                            item.id
                        }
                    ) { index, item ->
                        Row(
                            modifier = Modifier.padding(bottom = 15.dp, start = 5.dp)
                        ) {
                            Text(
                                text = "${index + 1}.",
                                fontSize = 19.sp,
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Column {
                                Text(
                                    text = item.agna,
                                    fontSize = 19.sp,
                                )

                                if (item.note.isNotEmpty()) {
                                    Text(
                                        text = "Note = ${item.note}",
                                        fontSize = 16.sp,
                                    )
                                }
                            }
                        }
                    }
                }

                if (state.remainingAgnaList.isNotEmpty()) {
                    item {
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(15.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = Color.Gray,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .size(45.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "${state.remainingAgnaList.size} - Agna Remaining to fill",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                    itemsIndexed(
                        state.remainingAgnaList,
                        key = { _, item ->
                            item.id
                        }
                    ) { index, item ->
                        Row(
                            modifier = Modifier.padding(bottom = 15.dp, start = 5.dp)
                        ) {
                            Text(
                                text = "${index + 1}.",
                                fontSize = 19.sp,
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Column {
                                Text(
                                    text = item.agna,
                                    fontSize = 19.sp,
                                )

                                if (item.note.isNotEmpty()) {
                                    Text(
                                        text = "Note = ${item.note}",
                                        fontSize = 16.sp,
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }

        PullToRefresh(
            pullToRefreshState = pullToRefreshState,
            isRefreshing = isRefreshing,
            onRefresh = {
                scope.launch {
                    isRefreshing = true
                    vm.onRefreshUiState()
                    delay(2000L)
                    isRefreshing = false
                }
            }
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PullToRefresh(
    pullToRefreshState: PullToRefreshState,
    isRefreshing: Boolean,
    onRefresh: () -> Unit
) {

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if (pullToRefreshState.isRefreshing) {
            LaunchedEffect(key1 = true) {
                onRefresh()
            }
        }

        LaunchedEffect(key1 = isRefreshing) {
            if (isRefreshing) {
                pullToRefreshState.startRefresh()
            } else {
                pullToRefreshState.endRefresh()
            }
        }

        PullToRefreshContainer(
            state = pullToRefreshState
        )
    }

}