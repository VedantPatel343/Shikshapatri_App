package com.swaminarayan.shikshapatriApp.presentation.screens.aeGoalScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.swaminarayan.shikshapatriApp.R
import com.swaminarayan.shikshapatriApp.presentation.components.CircularImage
import com.swaminarayan.shikshapatriApp.presentation.components.Page
import com.swaminarayan.shikshapatriApp.presentation.components.TopBar2Btn

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AEGoalScreen(
    goalId: Long,
    navController: NavHostController,
    vm: AEGoalsViewModel = hiltViewModel()
) {

    val goal by vm.goal.collectAsStateWithLifecycle()
    val goalError by vm.goalError.collectAsStateWithLifecycle()
    val isAchieved by vm.isAchieved.collectAsStateWithLifecycle()

    Page {
        TopBar2Btn(
            title = if (goalId == -1L) "Add Goal" else "Edit Goal",
            popBackStack = { navController.popBackStack() },
            onSaveClicked = {
                vm.onGoalSave()
            }
        )

        Column(
            modifier = Modifier.padding(
                horizontal = 15.dp,
                vertical = 10.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))

            CircularImage(image = R.drawable.maharaj1, size = 120.dp)

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(
                value = goal,
                onValueChange = { vm.onGoalChange(it) },
                singleLine = false,
                isError = goalError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Done
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = MaterialTheme.colorScheme.onSecondary,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(vertical = 10.dp),
                label = { Text(text = "Goal") },
                trailingIcon = {
                    if (goal.isNotEmpty()) {
                        IconButton(onClick = { vm.onGoalChange("") }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Is Goal Achieved?",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Switch(
                    checked = isAchieved,
                    onCheckedChange = {
                        vm.isAchievedChange()
                    }
                )
            }

        }

    }

}