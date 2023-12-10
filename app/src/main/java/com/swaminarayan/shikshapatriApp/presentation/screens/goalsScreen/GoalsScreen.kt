package com.swaminarayan.shikshapatriApp.presentation.screens.goalsScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.swaminarayan.shikshapatriApp.R
import com.swaminarayan.shikshapatriApp.domain.models.Goals
import com.swaminarayan.shikshapatriApp.presentation.components.BasicButton
import com.swaminarayan.shikshapatriApp.presentation.components.ConfirmMessage
import com.swaminarayan.shikshapatriApp.presentation.components.Page
import com.swaminarayan.shikshapatriApp.presentation.components.TopBar
import com.swaminarayan.shikshapatriApp.utils.dateFormatter
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsScreen(
    drawerState: DrawerState,
    navController: NavHostController,
    vm: GoalsViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()
    val goals by vm.goals.collectAsStateWithLifecycle()

    Page {
        TopBar(
            title = "All Goals",
            icon = Icons.Default.Menu,
            onBtnClick = { scope.launch { drawerState.open() } }
        )

        LazyColumn {
            items(goals, key = { it.id }) { goal ->

                GoalItem(
                    goal = goal,
                    onEditBtnClicked = {
                        navController.navigate("add_edit_goals_screen/${goal.id}")
                    },
                    onDeleteBtnClicked = {
                        vm.deleteGoals(goal)
                    }
                )

            }
        }

    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GoalItem(
    goal: Goals,
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
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(2.5.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary)
    ) {

        Column(Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = goal.goal,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )

                    Text(
                        text = dateFormatter(goal.dateTime),
                        modifier = Modifier.padding(top = 10.dp),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSecondary
                    )

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


            AnimatedVisibility(visible = dIsDesVisible) {
                Column(Modifier.fillMaxWidth()) {

                    Divider(color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        BasicButton(
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 10.dp),
                            text = "Delete",
                            onButtonClicked = { isDelMessageVisible = !isDelMessageVisible },
                            color = MaterialTheme.colorScheme.error
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