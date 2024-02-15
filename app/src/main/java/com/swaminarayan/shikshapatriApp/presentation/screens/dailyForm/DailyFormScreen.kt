package com.swaminarayan.shikshapatriApp.presentation.screens.dailyForm

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.swaminarayan.shikshapatriApp.R
import com.swaminarayan.shikshapatriApp.domain.models.DailyAgnaHelperClass
import com.swaminarayan.shikshapatriApp.presentation.components.LoadingAnimation
import com.swaminarayan.shikshapatriApp.presentation.components.Notice
import com.swaminarayan.shikshapatriApp.presentation.components.Page
import com.swaminarayan.shikshapatriApp.presentation.components.TopBar2Btn
import com.swaminarayan.shikshapatriApp.ui.theme.Green
import com.swaminarayan.shikshapatriApp.ui.theme.Red
import com.swaminarayan.shikshapatriApp.utils.UiEvents
import com.swaminarayan.shikshapatriApp.utils.dateFormatter
import com.swaminarayan.shikshapatriApp.utils.showToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyFormScreen(
    navController: NavHostController,
    vm: DailyFormViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val processedAgnaPalanList = vm.processedAgnas.toList().filter { it.palai == true }
    val processedAgnaLopList = vm.processedAgnas.toList().filter { it.palai == false }
    val remainingAgnas = vm.remainingAgnas.toList()
    val scope = rememberCoroutineScope()
    val isLoadingAnimationVisible = vm.isLoadingAnimationVisible
    val agnasSize = vm.agnas.size

    var isNoticeVisible by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true) {
        isNoticeVisible = true
        delay(6000)
        isNoticeVisible = false
    }

    val date = vm.date
    val formId = vm.formId

    LaunchedEffect(key1 = true) {
        vm.uiEventFlow.collectLatest {
            when (it) {
                is UiEvents.ShowToast -> {
                    showToast(context = context, message = it.message, isLenShort = it.isLenShort)
                }
            }
        }
    }

    Page {

        TopBar2Btn(
            title = "Daily Form",
            popBackStack = {
                navController.popBackStack()
            },
            onSaveClicked = {
                if (agnasSize == 0) {
                    showToast(context, "No Agnas are added.", false)
                } else {
                    scope.launch {
                        vm.onFormFilledClick()
                        if (remainingAgnas.isEmpty()) {
                            delay(1000)
                            navController.popBackStack()
                        }
                    }
                }
            }
        )

        if (agnasSize == 0) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No Agnas are added.", fontSize = 18.sp)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(text = "First add agna.", fontSize = 18.sp)
                }
            }
        } else {

            Column {
                Notice(
                    text = "Swipe agna right or left to fill the form",
                    isNoticeVisible = isNoticeVisible,
                    leftArrowColor = Green
                )

                DateRow(
                    date = date,
                    formId = formId,
                    onClick = {
                        scope.launch {
                            val id = vm.getIdByDate()
                            navController.navigate("single_day_report_screen/${id}")
                        }
                    }
                )

                LazyColumn {

                    item {
                        Text(
                            text = "Agnas:",
                            modifier = Modifier
                                .padding(10.dp),
                            fontSize = 18.sp
                        )
                    }
                    items(remainingAgnas) { agna ->
                        DailyAgnaItem(
                            agna,
                            agnaProcessed = { palai ->
                                vm.agnaProcessed(agna, palai, false)
                            },
                            isAgnaProcessed = false
                        )
                    }

                    item {
                        Text(
                            text = "Agna Palan:",
                            modifier = Modifier
                                .padding(10.dp),
                            fontSize = 18.sp
                        )
                    }
                    items(
                        processedAgnaPalanList,
                        key = { it.id }
                    )
                    { agna ->
                        DailyAgnaItem(
                            agna,
                            agnaProcessed = { palai ->
                                vm.agnaProcessed(agna, palai, true)
                            },
                            isAgnaProcessed = true
                        )
                    }

                    item {
                        Text(
                            text = "Agna Lop:",
                            modifier = Modifier
                                .padding(10.dp),
                            fontSize = 18.sp
                        )
                    }
                    items(
                        processedAgnaLopList,
                        key = { it.id }
                    ) { agna ->
                        DailyAgnaItem(
                            agna,
                            agnaProcessed = { palai ->
                                vm.agnaProcessed(agna, palai, true)
                            },
                            isAgnaProcessed = true
                        )
                    }

                }

                if (isLoadingAnimationVisible) {
                    LoadingAnimation(message = "Saving...")
                }

            }
        }
    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DateRow(date: LocalDate, formId: Long, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 5.dp)
            .padding(top = if (formId != -1L) 0.dp else 3.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(0.1f)
                .padding(3.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (formId != -1L) {
                Icon(
                    painter = painterResource(id = R.drawable.outlined_report_icon),
                    contentDescription = "report btn",
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
        Text(
            text = dateFormatter(date),
            fontSize = 20.sp,
            modifier = Modifier
                .weight(0.8f),
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier.weight(0.1f),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (formId != -1L) {
                IconButton(
                    onClick = {
                        onClick()
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.outlined_report_icon),
                        contentDescription = "report btn"
                    )
                }
            }
        }
    }
}


@Composable
private fun DailyAgnaItem(
    dailyAgna: DailyAgnaHelperClass,
    agnaProcessed: (palai: Boolean) -> Unit,
    isAgnaProcessed: Boolean
) {

    var isDesVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val dIsDesVisible by remember {
        derivedStateOf { isDesVisible }
    }

    val cardColor = if (!isAgnaProcessed) {
        MaterialTheme.colorScheme.secondary
    } else {
        if (dailyAgna.palai == true) {
            Green
        } else {
            Red
        }
    }

    val textColor = if (dailyAgna.palai == null) {
        MaterialTheme.colorScheme.onSecondary
    } else {
        Color.White
    }

    val palai = SwipeAction(
        onSwipe = {
            agnaProcessed(true)
        },
        icon = {
            Text(
                text = "Agna Palan",
                color = Color.White,
                modifier = Modifier.padding(end = 20.dp),
                fontSize = 20.sp
            )
        },
        background = Green
    )

    val naPalai = SwipeAction(
        onSwipe = {
            agnaProcessed(false)
        },
        icon = {
            Text(
                text = "Agna Lop",
                color = Color.White,
                modifier = Modifier.padding(start = 20.dp),
                fontSize = 20.sp
            )
        },
        background = Red
    )

    SwipeableActionsBox(
        startActions = listOf(palai),
        endActions = listOf(naPalai),
        swipeThreshold = 120.dp,
        modifier = Modifier.padding(bottom = 10.dp)
    ) {

        Box(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(3.dp),
                colors = CardDefaults.cardColors(cardColor)
            ) {

                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .padding(top = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                        ) {

                            Text(
                                text = dailyAgna.agna,
                                fontSize = 18.sp,
                                color = textColor
                            )

                            if (dailyAgna.slokNo != 0) {
                                Text(
                                    text = "Slok No - ${dailyAgna.slokNo}",
                                    modifier = Modifier.padding(top = 10.dp),
                                    fontSize = 18.sp,
                                    color = textColor
                                )
                            }

                            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                IconButton(
                                    onClick = {
                                        isDesVisible = !isDesVisible
                                    }
                                ) {
                                    Icon(
                                        painter = if (dIsDesVisible) {
                                            painterResource(id = R.drawable.up_arrow_icon)
                                        } else {
                                            painterResource(id = R.drawable.down_arrow_icon)
                                        }, contentDescription = "",
                                        tint = textColor
                                    )
                                }
                            }

                        }

                    }


                    AnimatedVisibility(visible = dIsDesVisible) {
                        Column(Modifier.fillMaxWidth()) {

                            Divider(color = MaterialTheme.colorScheme.background)

                            Text(
                                text = "Des - ${dailyAgna.description}",
                                modifier = Modifier.padding(top = 10.dp),
                                fontSize = 18.sp,
                                color = textColor
                            )

                            Text(
                                text = "Author - ${dailyAgna.author}",
                                modifier = Modifier.padding(top = 10.dp),
                                fontSize = 18.sp,
                                color = textColor
                            )

                            Text(
                                text = "Rajipo Points - ${dailyAgna.rajipoPoints}",
                                modifier = Modifier.padding(top = 10.dp),
                                fontSize = 18.sp,
                                color = textColor
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                        }
                    }
                }

            }

        }
    }
}