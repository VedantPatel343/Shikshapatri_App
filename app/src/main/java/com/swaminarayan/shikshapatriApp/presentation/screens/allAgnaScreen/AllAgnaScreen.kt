package com.swaminarayan.shikshapatriApp.presentation.screens.allAgnaScreen

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.swaminarayan.shikshapatriApp.R
import com.swaminarayan.shikshapatriApp.domain.models.Agna
import com.swaminarayan.shikshapatriApp.presentation.components.BasicButton
import com.swaminarayan.shikshapatriApp.presentation.components.ConfirmMessage
import com.swaminarayan.shikshapatriApp.presentation.components.Page
import com.swaminarayan.shikshapatriApp.presentation.components.TopBar
import com.swaminarayan.shikshapatriApp.utils.UiEvents
import com.swaminarayan.shikshapatriApp.utils.showToast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllAgnaScreen(
    drawerState: DrawerState,
    navController: NavHostController,
    vm: AllAgnaViewModel = hiltViewModel()
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val agnas by vm.agnas.collectAsStateWithLifecycle()

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
        TopBar(
            title = "All Agnas",
            icon = Icons.Default.Menu,
            onBtnClick = { scope.launch { drawerState.open() } }
        )

        LazyColumn {
            items(agnas, key = { it.id }) { agna ->
                AgnaItem(
                    agna,
                    onDeleteBtnClicked = {
                        vm.deleteAgna(agna)
                    },
                    onEditBtnClicked = {
                        navController.navigate("add_edit_agna_screen/${agna.id}")
                    },
                    onStarClicked = {
                        vm.onStarClicked(agna)
                    }
                )
            }
        }

    }

}

@Composable
fun AgnaItem(
    agna: Agna,
    onStarClicked: () -> Unit,
    onEditBtnClicked: () -> Unit,
    onDeleteBtnClicked: () -> Unit
) {

    var isDesVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val dIsDesVisible by remember {
        derivedStateOf { isDesVisible }
    }

    var isDelMessageVisible by rememberSaveable {
        mutableStateOf(false)
    }
    val dIsDelMessageVisible by remember {
        derivedStateOf { isDelMessageVisible }
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 10.dp, end = 10.dp),
        elevation = CardDefaults.cardElevation(2.5.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary)
    ) {

        Column(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, start = 10.dp, end = 10.dp)
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
                        text = agna.agna,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )

                    if (agna.slokNo != 0) {
                        Text(
                            text = "Slok No - ${agna.slokNo}",
                            modifier = Modifier.padding(top = 10.dp),
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                    }

                    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        IconButton(
                            onClick = {
                                isDesVisible = !isDesVisible
                                isDelMessageVisible = false
                            }
                        ) {
                            Icon(
                                painter = if (dIsDesVisible) {
                                    painterResource(id = R.drawable.up_arrow_icon)
                                } else {
                                    painterResource(id = R.drawable.down_arrow_icon)
                                }, contentDescription = "",
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                }

                IconButton(
                    onClick = {
                        onStarClicked()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = if (agna.isStared) {
                            Icons.Default.Star
                        } else {
                            Icons.Outlined.Star
                        }, contentDescription = "",
                        tint = if (agna.isStared) {
                            Color.Yellow
                        } else {
                            MaterialTheme.colorScheme.onSecondary
                        }
                    )
                }

            }


            AnimatedVisibility(visible = dIsDesVisible) {
                Column(Modifier.fillMaxWidth()) {

                    Text(
                        text = "Des - ${agna.description}",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )

                    Text(
                        text = "Author - ${agna.author}",
                        modifier = Modifier.padding(top = 10.dp),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )

                    Text(
                        text = "Rajipo Points - ${agna.rajipoPoints}",
                        modifier = Modifier.padding(top = 10.dp),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )

                    Text(
                        text = "Always palay che? - ${if (agna.alwaysPalayChe) "YES" else "NO"}",
                        modifier = Modifier.padding(top = 10.dp),
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = MaterialTheme.colorScheme.onSecondary)
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                    ) {
                        BasicButton(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 10.dp),
                            text = "Edit",
                            onButtonClicked = {
                                onEditBtnClicked()
                                isDesVisible = false
                            },
                            color = MaterialTheme.colorScheme.onSecondary
                        )
                        BasicButton(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 10.dp),
                            text = "Delete",
                            onButtonClicked = { isDelMessageVisible = !isDelMessageVisible },
                            color = Color.Red
                        )
                    }
                }
            }

            ConfirmMessage(
                isMessageVisible = dIsDelMessageVisible,
                onButtonClick = {
                    onDeleteBtnClicked()
                    isDelMessageVisible = false
                },
                titleText = "Confirm Delete?",
                buttonText = "DELETE"
            )

        }

    }

}
