package com.swaminarayan.shikshapatriApp.presentation.screens.allAgnaScreen

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.swaminarayan.shikshapatriApp.R
import com.swaminarayan.shikshapatriApp.domain.models.Agna
import com.swaminarayan.shikshapatriApp.presentation.components.ConfirmMessage
import com.swaminarayan.shikshapatriApp.presentation.components.Notice
import com.swaminarayan.shikshapatriApp.presentation.components.Page
import com.swaminarayan.shikshapatriApp.ui.theme.Red
import com.swaminarayan.shikshapatriApp.ui.theme.backgroundL
import com.swaminarayan.shikshapatriApp.utils.UiEvents
import com.swaminarayan.shikshapatriApp.utils.showToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun AllAgnaScreen(
    navController: NavHostController,
    vm: AllAgnaViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val state by vm.state.collectAsStateWithLifecycle()


    var isNoticeVisible by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true) {
        isNoticeVisible = true
        delay(6000)
        isNoticeVisible = false
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

        Box(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .padding(top = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "All Agnas",
                fontSize = 30.sp,
                fontFamily = FontFamily.Cursive,
                color = MaterialTheme.colorScheme.primary
            )
        }

        if (state.agnas.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No Agna is added.")
            }
        } else {
            Notice(
                text = "Swipe agna right or left to edit or delete it.",
                isNoticeVisible = isNoticeVisible,
                leftArrowColor = Color.Gray
            )
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn {
                items(state.agnas, key = { it.id }) { agna ->
                    AgnaItem(
                        agna,
                        editAgnaFun = {
                            navController.navigate("add_edit_agna_screen/${agna.id}")
                        },
                        deleteAgnaFun = {
                            vm.deleteAgna(agna)
                        }
                    )
                }
            }
        }

    }

}

@Composable
fun AgnaItem(
    agna: Agna,
    editAgnaFun: () -> Unit,
    deleteAgnaFun: () -> Unit
) {

    var isDesVisible by rememberSaveable {
        mutableStateOf(false)
    }

    var isDelMessageVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val editAgna = SwipeAction(
        onSwipe = {
            editAgnaFun()
        },
        icon = {
            Text(
                text = "Edit",
                color = Color.White,
                modifier = Modifier.padding(end = 20.dp),
                fontSize = 20.sp
            )
        },
        background = Color.Gray
    )

    val deleteAgna = SwipeAction(
        onSwipe = {
            isDelMessageVisible = true
        },
        icon = {
            Text(
                text = "Delete",
                color = Color.White,
                modifier = Modifier.padding(start = 20.dp),
                fontSize = 20.sp
            )
        },
        background = Red
    )


    SwipeableActionsBox(
        startActions = listOf(editAgna),
        endActions = listOf(deleteAgna),
        swipeThreshold = 100.dp,
        modifier = Modifier.padding(bottom = 13.dp, top = 2.dp)
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
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
            ) {

                Column(
                    Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(
                                text = agna.agna,
                                fontSize = 20.sp,
                                textAlign = TextAlign.Start,
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 20.dp)
                                    .weight(9f)
                            )

                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                                    .padding(end = 2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                IconButton(
                                    onClick = {
                                        isDesVisible = !isDesVisible
                                        isDelMessageVisible = false
                                    }
                                ) {
                                    Icon(
                                        painter = if (isDesVisible) {
                                            painterResource(id = R.drawable.up_arrow_icon)
                                        } else {
                                            painterResource(id = R.drawable.down_arrow_icon)
                                        }, contentDescription = ""
                                    )
                                }
                            }
                        }

                    }


                    AnimatedVisibility(visible = isDesVisible) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp)
                        ) {

                            Divider()
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Des - ${agna.description}",
                                fontSize = 18.sp
                            )

                            Text(
                                text = "Author - ${agna.author}",
                                modifier = Modifier.padding(top = 10.dp),
                                fontSize = 18.sp
                            )

                            Text(
                                text = "Rajipo Points - ${agna.rajipoPoints}",
                                modifier = Modifier.padding(top = 10.dp),
                                fontSize = 18.sp
                            )

                            Text(
                                text = "Is counter? - ${if (agna.isCounter) "YES" else "NO"}",
                                modifier = Modifier.padding(top = 10.dp),
                                fontSize = 18.sp
                            )

                            Text(
                                text = "Always palay che? - ${if (agna.alwaysPalayChe) "YES" else "NO"}",
                                modifier = Modifier.padding(top = 10.dp),
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }
                    }

                    ConfirmMessage(
                        isMessageVisible = isDelMessageVisible,
                        onDeleteClick = {
                            deleteAgnaFun()
                            isDelMessageVisible = false
                        },
                        onCancelClick = { isDelMessageVisible = false },
                        titleText = "Confirm Delete?"
                    )
                }
            }
        }
    }
}