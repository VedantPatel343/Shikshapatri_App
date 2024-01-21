package com.swaminarayan.shikshapatriApp.presentation.screens.dailyForm

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
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
import com.swaminarayan.shikshapatriApp.presentation.Screens
import com.swaminarayan.shikshapatriApp.presentation.components.LoadingAnimation
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

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DailyFormScreen(
    navController: NavHostController,
    vm: DailyFormViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val processedPalaiAgnas = vm.processedAgnas.toList().filter { it.palai == true }
    val processedNaPalaiAgnas = vm.processedAgnas.toList().filter { it.palai == false }
    val remainingAgnas = vm.remainingAgnas.toList()
    val scope = rememberCoroutineScope()
    val isLoadingAnimationVisible = vm.isLoadingAnimationVisible

    val liveScore = vm.liveScore
    val date = vm.date

    BackHandler {
        navController.popBackStack()
        navController.navigate(Screens.HomeScreen.route)
    }

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
                navController.navigate(Screens.HomeScreen.route)
            },
            onSaveClicked = {
                scope.launch {
                    vm.onFormFilledClick()
                    if (remainingAgnas.isEmpty()) {
                        delay(4000)
                        navController.popBackStack()
                        navController.navigate(Screens.HomeScreen.route)
                    }
                }
            }
        )

        Column {
            Text(
                text = dateFormatter(date),
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Live Score = $liveScore",
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(10.dp)
            )
            LazyColumn {

                item {
                    Log.i("listTest", "remaining: ${remainingAgnas.toList()}")
                    Log.i("listTest", "palai: ${processedPalaiAgnas.toList()}")
                    Log.i("listTest", "na palai: ${processedNaPalaiAgnas.toList()}")
                    Text(
                        text = "Agnas:",
                        modifier = Modifier
                            .padding(10.dp),
                        fontSize = 18.sp
                    )
                }
                items(remainingAgnas) { agnas ->
                    DailyAgnaItem(
                        agnas,
                        agnaProcessed = { palai ->
                            vm.agnaProcessed(agnas, palai)
                        },
                        isAgnaProcessed = false
                    )
                }

                item {
                    Text(
                        text = "Agna Palai:",
                        modifier = Modifier
                            .padding(10.dp),
                        fontSize = 18.sp
                    )
                }
                items(processedPalaiAgnas) { helper ->
                    DailyAgnaItem(
                        helper,
                        agnaProcessed = { palai ->
                            vm.agnaProcessed(helper, palai)
                        },
                        isAgnaProcessed = true
                    )
                }

                item {
                    Text(
                        text = "Agna Na Palai:",
                        modifier = Modifier
                            .padding(10.dp),
                        fontSize = 18.sp
                    )
                }
                items(processedNaPalaiAgnas) { helper ->
                    DailyAgnaItem(
                        helper,
                        agnaProcessed = { palai ->
                            vm.agnaProcessed(helper, palai)
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
                text = "Palai",
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
                text = "Na Palai",
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
        swipeThreshold = 100.dp,
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

                            Spacer(modifier = Modifier.height(5.dp))

                            Row {
//                                Text(
//                                    text = "Agna - ",
//                                    fontSize = 18.sp,
//                                    color = textColor
//                                )

                                Text(
                                    text = dailyAgna.agna,
                                    fontSize = 18.sp,
                                    color = textColor
                                )
                            }

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

                            Divider(color = MaterialTheme.colorScheme.onSurfaceVariant)

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

                            Text(
                                text = "Always palay che? - ${if (dailyAgna.alwaysPalayChe) "YES" else "NO"}",
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