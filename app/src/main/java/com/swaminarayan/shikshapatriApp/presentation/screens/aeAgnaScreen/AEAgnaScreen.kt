package com.swaminarayan.shikshapatriApp.presentation.screens.aeAgnaScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
    val agna by vm.agna.collectAsStateWithLifecycle()
    val des by vm.des.collectAsStateWithLifecycle()
    val author by vm.author.collectAsStateWithLifecycle()
    val slokNo by vm.slokNo.collectAsStateWithLifecycle()
    val points by vm.rajipoPoints.collectAsStateWithLifecycle()
    val alwaysPalayChe by vm.alwaysPalayChe.collectAsStateWithLifecycle()
    val isStared by vm.isStared.collectAsStateWithLifecycle()

    val agnaError by vm.agnaError.collectAsStateWithLifecycle()
    val authorError by vm.authorError.collectAsStateWithLifecycle()
    val rajipoPointsError by vm.rajipoPointsError.collectAsStateWithLifecycle()

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
                if (validateErrors(vm)) {
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

            Spacer(modifier = Modifier.height(3.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(bottom = 5.dp),
                value = agna,
                onValueChange = {
                    vm.onEvent(AEAgnaEvents.OnAgnaChange(it))
                },
                singleLine = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                isError = agnaError,
                trailingIcon = {
                    if (agna.isNotEmpty()) {
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
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    errorBorderColor = Color.Red,
                    errorCursorColor = Color.Red,
                    errorLabelColor = Color.Red
                )
            )

            OutlinedTextField(
                value = des,
                onValueChange = { vm.onEvent(AEAgnaEvents.OnDesChange(it)) },
                singleLine = false,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
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
                    if (des.isNotEmpty()) {
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
                text = author,
                onTextChanged = { vm.onEvent(AEAgnaEvents.OnAuthorChange(it)) },
                onClearTextClicked = { vm.onEvent(AEAgnaEvents.OnAuthorChange("")) },
                label = "Author",
                isError = authorError,
                keyBoardType = KeyboardType.Text,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(5.dp))

            OTF(
                text = slokNo,
                onTextChanged = { vm.onEvent(AEAgnaEvents.OnSlokNoChange(it)) },
                onClearTextClicked = { vm.onEvent(AEAgnaEvents.OnSlokNoChange("")) },
                label = "Slok No.",
                isError = false,
                keyBoardType = KeyboardType.Number,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(5.dp))

            OTF(
                text = points,
                onTextChanged = { vm.onEvent(AEAgnaEvents.OnPointsChange(it)) },
                onClearTextClicked = { vm.onEvent(AEAgnaEvents.OnPointsChange("")) },
                label = "Rajipo Points",
                isError = rajipoPointsError,
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

fun validateErrors(vm: AEAgnaViewModel): Boolean {
    return when {
        vm.agnaError.value -> {
            false
        }

        vm.authorError.value -> {
            false
        }

        vm.rajipoPointsError.value -> {
            false
        }

        else -> {
            true
        }
    }
}
