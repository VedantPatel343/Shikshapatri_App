package com.swaminarayan.shikshapatriApp.presentation.screens.aeAgnaScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.swaminarayan.shikshapatriApp.presentation.components.OTFWithError
import com.swaminarayan.shikshapatriApp.presentation.components.Page
import com.swaminarayan.shikshapatriApp.presentation.components.TopBar2Btn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AEAgnaScreen(
    agnaId: Long,
    navController: NavHostController,
    vm: AEAgnaViewModel
) {
    val agna by vm.agna.collectAsStateWithLifecycle()
    val des by vm.des.collectAsStateWithLifecycle()
    val author by vm.author.collectAsStateWithLifecycle()
    val slokNo by vm.slokNo.collectAsStateWithLifecycle()
    val points by vm.points.collectAsStateWithLifecycle()
    val alwaysPalayChe by vm.alwaysPalayChe.collectAsStateWithLifecycle()
    val isStared by vm.isStared.collectAsStateWithLifecycle()

    val agnaError by vm.agnaError.collectAsStateWithLifecycle()

    Page {
        // TOP BAR
        TopBar2Btn(title = if (agnaId == -1L) "Add Agna" else "Edit Agna",
            popBackStack = { navController.popBackStack() },
            onSaveClicked = { vm.onEvent(AEAgnaEvents.OnSaveAgna) }
        )

        Column(
            modifier = Modifier.padding(
                horizontal = 15.dp,
                vertical = 10.dp
            )
        ) {

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                IconToggleButton(
                    checked = isStared,
                    onCheckedChange = { vm.onEvent(AEAgnaEvents.OnIsStaredChange(it)) },
                ) {
                    Icon(
                        imageVector = if (isStared) Icons.Default.Star else Icons.Default.StarOutline,
                        contentDescription = null,
                        tint = if (isStared) Color.Yellow else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            OTFWithError(
                text = agna,
                onTextChanged = { vm.onEvent(AEAgnaEvents.OnAgnaChange(it)) },
                onClearTextClicked = { vm.onEvent(AEAgnaEvents.OnAgnaChange("")) },
                label = "Agna",
                isError = agnaError,
                keyBoardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = des,
                onValueChange = { vm.onEvent(AEAgnaEvents.OnDesChange(it)) },
                singleLine = false,
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
                    .height(150.dp)
                    .padding(vertical = 10.dp),
                label = { Text(text = "Description") },
                trailingIcon = {
                    if (des.isNotEmpty()) {
                        IconButton(onClick = { vm.onEvent(AEAgnaEvents.OnDesChange("")) }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                }
            )

            OTFWithError(
                text = author,
                onTextChanged = { vm.onEvent(AEAgnaEvents.OnAuthorChange(it)) },
                onClearTextClicked = { vm.onEvent(AEAgnaEvents.OnAuthorChange("")) },
                label = "Author",
                isError = false,
                keyBoardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OTFWithError(
                text = slokNo,
                onTextChanged = { vm.onEvent(AEAgnaEvents.OnSlokNoChange(it)) },
                onClearTextClicked = { vm.onEvent(AEAgnaEvents.OnSlokNoChange("")) },
                label = "Slok No.",
                isError = false,
                keyBoardType = KeyboardType.Number,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OTFWithError(
                text = points,
                onTextChanged = { vm.onEvent(AEAgnaEvents.OnPointsChange(it)) },
                onClearTextClicked = { vm.onEvent(AEAgnaEvents.OnPointsChange("")) },
                label = "Rajipo Points",
                isError = false,
                keyBoardType = KeyboardType.Number,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(13.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Always Agna palay che?",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Switch(
                    checked = alwaysPalayChe,
                    onCheckedChange = {
                        vm.onEvent(AEAgnaEvents.OnAlwaysPalayCheChange(it))
                    }
                )
            }

        }
    }
}

@Preview
@Composable
private fun Preview() {
    AEAgnaScreen(agnaId = -1L, navController = rememberNavController(), vm = viewModel())
}