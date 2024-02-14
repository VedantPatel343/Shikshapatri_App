package com.swaminarayan.shikshapatriApp.presentation.screens.homeScreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.swaminarayan.shikshapatriApp.calender.CalenderDataSource
import com.swaminarayan.shikshapatriApp.calender.ui.Calender
import com.swaminarayan.shikshapatriApp.presentation.components.CircularImage
import com.swaminarayan.shikshapatriApp.presentation.components.Page
import com.swaminarayan.shikshapatriApp.ui.theme.Green
import com.swaminarayan.shikshapatriApp.ui.theme.Red
import com.swaminarayan.shikshapatriApp.utils.dateFormatter
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    navController: NavHostController,
    vm: HomeViewModel = hiltViewModel(),
    maharaj: Int,
    guruji: Int
) {

    val scope = rememberCoroutineScope()
    val notes by vm.notes.collectAsStateWithLifecycle()

    var showDialog by rememberSaveable {
        mutableStateOf(false)
    }
    val sloganText by vm.slogan.collectAsStateWithLifecycle()
    val firstDayOfWeek by vm.firstDayOfWeek.collectAsStateWithLifecycle()
    val lastDayOfWeek by vm.lastDayOfWeek.collectAsStateWithLifecycle()
    val calenderData = CalenderDataSource()
    val visibleDateList = calenderData.getVisibleDates(startDate = firstDayOfWeek)
    val dateList by vm.dailyFormDateList.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = true) {
        val flag = vm.flag
        if (!flag) {
            vm.setUpList(firstDayOfWeek, lastDayOfWeek)
        }
        if (flag) {
            vm.changeFlagValue()
        }
    }

    val dates = if (firstDayOfWeek == LocalDate.now()) {
        dateFormatter(LocalDate.now())
    } else {
        "${dateFormatter(firstDayOfWeek)}   -   ${
            dateFormatter(
                if (visibleDateList.contains(LocalDate.now()))
                    LocalDate.now()
                else
                    lastDayOfWeek
            )
        }"
    }

    Page {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
                .padding(top = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Shikshapatri",
                fontSize = 30.sp,
                fontFamily = FontFamily.Cursive,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Box(
            modifier = Modifier
                .padding(top = 5.dp, bottom = 30.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularImage(
                    image = maharaj,
                    size = 160.dp
                )
                CircularImage(
                    image = guruji,
                    size = 160.dp
                )
            }
        }

        LazyColumn {
            item {
                Slogan(
                    onLongClick = {
                        showDialog = true
                    },
                    text = sloganText
                )
                Box(modifier = Modifier.padding(horizontal = 5.dp)) {
                    Calender(
                        centerText = dates,
                        visibleDateList = visibleDateList,
                        onDateClicked = { date ->
                            scope.launch {
                                val id = vm.getIdByDate(date)
                                navController.navigate("daily_form_screen/${id}/$date")
                            }
                        },
                        onNextClick = {
                            vm.onNextDateClicked()
                        },
                        onPreviousClick = {
                            vm.onPrevDateClicked()
                        },
                        showPrevNextBtn = true,
                        showTickMarks = {
                            dateList.contains(it)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(25.dp))
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = "Notes:",
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(15.dp))

                if (showDialog) {
                    UpdateTextDialog(
                        onDismissClicked = { showDialog = false },
                        onSaveClicked = {
                            vm.updateSloganText(it)
                            showDialog = false
                        },
                        textValue = sloganText
                    )
                }


            }

            itemsIndexed(
                items = notes,
                key = { _, note ->
                    note.id
                }
            ) { index, note ->
                NotesItem(
                    index = index + 1,
                    note = note.des
                )
            }

        }

    }
}

@Composable
private fun NotesItem(note: String, index: Int) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp)
            .padding(horizontal = 10.dp)
    ) {
        Text(text = "$index.")
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = note)
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Slogan(onLongClick: () -> Unit, text: String) {
    Divider(color = MaterialTheme.colorScheme.primary)
    Text(
        text = text,
        fontSize = 18.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .combinedClickable(
                onLongClick = {
                    onLongClick()
                },
                onClick = {}
            ),
        textAlign = TextAlign.Center
    )
    Divider(color = MaterialTheme.colorScheme.primary)
    Spacer(modifier = Modifier.height(15.dp))
}

@Composable
fun UpdateTextDialog(
    textValue: String,
    onDismissClicked: () -> Unit,
    onSaveClicked: (String) -> Unit,
) {

    var text by rememberSaveable {
        mutableStateOf(textValue)
    }

    Dialog(
        onDismissRequest = { onDismissClicked() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnClickOutside = true,
            dismissOnBackPress = true
        )
    ) {
        Card(
            elevation = CardDefaults.cardElevation(6.dp),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .border(
                    2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(15.dp)
                ),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(top = 5.dp, bottom = 15.dp),
                horizontalAlignment = Alignment.End
            ) {

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    isError = false,
                    trailingIcon = {
                        if (text.isNotEmpty()) {
                            Icon(
                                modifier = Modifier
                                    .clickable { text = "" }
                                    .padding(end = 3.dp)
                                    .size(20.dp),
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    },
                    singleLine = false,
                    label = { Text(text = "") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary,
                        errorBorderColor = Color.Red,
                        errorCursorColor = Color.Red,
                        errorLabelColor = Color.Red
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row {
                    OutlinedButton(
                        onClick = { onDismissClicked() },
                        border = BorderStroke(1.5.dp, Red),
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .height(40.dp)
                            .weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "",
                            tint = Red,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    OutlinedButton(
                        onClick = { onSaveClicked(text) },
                        border = BorderStroke(1.5.dp, Green),
                        shape = RoundedCornerShape(5.dp),
                        modifier = Modifier
                            .height(40.dp)
                            .weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "",
                            tint = Green,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }


            }
        }
    }
}