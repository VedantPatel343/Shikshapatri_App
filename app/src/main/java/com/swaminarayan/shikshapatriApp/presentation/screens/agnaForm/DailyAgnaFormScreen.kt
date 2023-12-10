package com.swaminarayan.shikshapatriApp.presentation.screens.agnaForm

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.swaminarayan.shikshapatriApp.R
import com.swaminarayan.shikshapatriApp.domain.models.DailyAgnas
import com.swaminarayan.shikshapatriApp.presentation.components.Page
import com.swaminarayan.shikshapatriApp.presentation.components.TopBar2Btn
import com.swaminarayan.shikshapatriApp.ui.theme.greenC

@Composable
fun DailyAgnaFormScreen(
    navController: NavHostController,
    vm: DailyAgnaFormViewModel = hiltViewModel()
) {

    val processedAgnas = vm.processedAgnas.toList()
    val remainingAgnas = vm.remainingAgnas.toList()


    Page {

        TopBar2Btn(
            title = "Daily Agna Form",
            popBackStack = { navController.popBackStack() },
            onSaveClicked = { vm.onFormFilledClick() }
        )

        LazyColumn {

            items(remainingAgnas, key = { it.id }) { helper ->
                DailyAgnaItem(
                    helper,
                    agnaProcessed = { palai ->
                        vm.agnaProcessed(helper, palai)
                    },
                    false
                )
            }

            items(processedAgnas, key = { it.id }) { helper ->
                DailyAgnaItem(
                    helper,
                    agnaProcessed = { palai ->
                        vm.agnaProcessed(helper, palai)
                    },
                    true
                )
            }

        }

    }

}


@Composable
private fun DailyAgnaItem(
    dailyAgna: DailyAgnas,
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
            greenC
        } else {
            Color.Red
        }
    }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            elevation = CardDefaults.cardElevation(2.5.dp),
            colors = CardDefaults.cardColors(cardColor)
        ) {

            Column(Modifier.fillMaxWidth()) {
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

                        Text(
                            text = "Agna - ${dailyAgna.agna}",
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )

                        Text(
                            text = "Author - ${dailyAgna.author}",
                            modifier = Modifier.padding(top = 15.dp),
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )


                        if (!isAgnaProcessed) {
                            Spacer(modifier = Modifier.height(15.dp))
                            Divider(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(modifier = Modifier.height(15.dp))
                            Buttons(
                                agnaProcessed = {
                                    agnaProcessed(it)
                                },
                                color = null
                            )
                        }

                        if (isAgnaProcessed) Spacer(modifier = Modifier.height(15.dp))

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
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        }

                    }

                }


                AnimatedVisibility(visible = dIsDesVisible) {
                    Column(Modifier.fillMaxWidth()) {

                        if (isAgnaProcessed) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Buttons(
                                agnaProcessed = {
                                    agnaProcessed(it)
                                },
                                color = Color.White
                            )
                        }

                        Divider(color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Des - ${dailyAgna.description}",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )

                        Text(
                            text = "Slok No - ${dailyAgna.slokNo}",
                            modifier = Modifier.padding(top = 10.dp),
                            fontSize = 20.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )

                        Text(
                            text = "Rajipo Points - ${dailyAgna.points}",
                            modifier = Modifier.padding(top = 10.dp),
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )

                        Text(
                            text = "Always palay che? - ${if (dailyAgna.alwaysPalayChe) "YES" else "NO"}",
                            modifier = Modifier.padding(top = 10.dp),
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSecondary
                        )

                    }
                }

            }

        }


}

@Composable
fun Buttons(agnaProcessed: (palai: Boolean) -> Unit, color: Color?) {

    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        OutlinedButton(
            onClick = { agnaProcessed(false) },
            border = BorderStroke(1.5.dp, color ?: Color.Red),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .height(60.dp)
                .weight(1f)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.cancel_icon),
                contentDescription = "Na Palai",
                tint = color ?: Color.Red,
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        OutlinedButton(
            onClick = { agnaProcessed(true) },
            border = BorderStroke(1.5.dp, color ?: greenC),
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .height(60.dp)
                .weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = "Palai",
                tint = color ?: greenC,
                modifier = Modifier.size(28.dp)
            )
        }

    }
}