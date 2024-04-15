package com.swaminarayan.shikshapatriApp.presentation.screens.aeAgnaScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.swaminarayan.shikshapatriApp.presentation.components.OTF
import com.swaminarayan.shikshapatriApp.presentation.components.Page
import com.swaminarayan.shikshapatriApp.presentation.components.TopBar2Btn
import com.swaminarayan.shikshapatriApp.utils.UiEvents
import com.swaminarayan.shikshapatriApp.utils.showToast
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AEAgnaScreen(
    agnaId: Long,
    navController: NavHostController,
    vm: AEAgnaViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val localFocus = LocalFocusManager.current
    val state by vm.state.collectAsStateWithLifecycle()

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
        // TOP BAR
        TopBar2Btn(title = if (agnaId == -1L) "Add Agna" else "Edit Agna",
            popBackStack = { navController.popBackStack() },
            onSaveClicked = {
                vm.onEvent(AEAgnaEvents.OnSaveAgna)
                if (!state.agnaError) {
                    navController.popBackStack()
                }
            }
        )

        Column(
            modifier = Modifier.padding(
                horizontal = 15.dp,
                vertical = 10.dp
            )
        ) {

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(bottom = 5.dp),
                value = state.agna,
                onValueChange = {
                    vm.onEvent(AEAgnaEvents.OnAgnaChange(it))
                },
                singleLine = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                isError = state.agnaError,
                trailingIcon = {
                    if (state.agna.isNotEmpty()) {
                        Icon(
                            modifier = Modifier
                                .clickable { vm.onEvent(AEAgnaEvents.OnAgnaChange("")) }
                                .padding(end = 3.dp)
                                .size(20.dp),
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear"
                        )
                    }
                },
                label = { Text(text = "Agna") },
                keyboardActions = KeyboardActions(onNext = { localFocus.moveFocus(FocusDirection.Down) }),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    errorBorderColor = Color.Red,
                    errorCursorColor = Color.Red,
                    errorLabelColor = Color.Red
                )
            )

            OutlinedTextField(
                value = state.des,
                onValueChange = { vm.onEvent(AEAgnaEvents.OnDesChange(it)) },
                singleLine = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(bottom = 5.dp),
                keyboardActions = KeyboardActions(onNext = { localFocus.moveFocus(FocusDirection.Down) }),
                label = { Text(text = "Description") },
                trailingIcon = {
                    if (state.des.isNotEmpty()) {
                        IconButton(onClick = { vm.onEvent(AEAgnaEvents.OnDesChange("")) }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    }
                }
            )

            OTF(
                text = state.author,
                onTextChanged = { vm.onEvent(AEAgnaEvents.OnAuthorChange(it)) },
                onClearTextClicked = { vm.onEvent(AEAgnaEvents.OnAuthorChange("")) },
                label = "Author",
                isError = false,
                keyBoardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(5.dp))

            OTF(
                text = state.rajipoPoints,
                onTextChanged = { vm.onEvent(AEAgnaEvents.OnPointsChange(it)) },
                onClearTextClicked = { vm.onEvent(AEAgnaEvents.OnPointsChange("")) },
                label = "Rajipo Points",
                isError = false,
                keyBoardType = KeyboardType.Number,
                modifier = Modifier.fillMaxWidth(),
                imeAction = ImeAction.Done
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
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Switch(
                    checked = state.alwaysPalayChe,
                    onCheckedChange = {
                        vm.onEvent(AEAgnaEvents.IsAlwaysPalayCheChange(it))
                    }
                )
            }

            Spacer(modifier = Modifier.height(13.dp))

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 3.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Is Counter?",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Switch(
                    checked = state.isCounter,
                    onCheckedChange = {
                        vm.onEvent(AEAgnaEvents.IsCounterChange(it))
                    }
                )
            }

        }
    }
}