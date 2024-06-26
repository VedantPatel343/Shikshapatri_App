package com.swaminarayan.shikshapatriApp.presentation.screens.noteScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.swaminarayan.shikshapatriApp.domain.models.Note
import com.swaminarayan.shikshapatriApp.presentation.components.ConfirmMessage
import com.swaminarayan.shikshapatriApp.presentation.components.Notice
import com.swaminarayan.shikshapatriApp.presentation.components.Page
import com.swaminarayan.shikshapatriApp.ui.theme.Red
import com.swaminarayan.shikshapatriApp.utils.showToast
import kotlinx.coroutines.delay
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@Composable
fun NotesScreen(
    navController: NavHostController,
    vm: NotesViewModel = hiltViewModel()
) {

    val notes by vm.notes.collectAsStateWithLifecycle()

    var isNoticeVisible by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = true) {
        isNoticeVisible = true
        delay(6000)
        isNoticeVisible = false
    }

    Scaffold(
        floatingActionButton = {

            FloatingActionButton(
                onClick = { navController.navigate("add_edit_notes_screen/${-1L}") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.background
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                )
            }
        }
    ) { paddingValue ->
        Page(modifier = Modifier.padding(paddingValue)) {

            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
                    .padding(top = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Notes",
                    fontSize = 30.sp,
                    fontFamily = FontFamily.Cursive,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (notes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No notes are added.")
                }
            } else {
                Notice(
                    text = "Swipe agna right or left to edit or delete it.",
                    isNoticeVisible = isNoticeVisible,
                    leftArrowColor = Color.Gray,
                    isLeftIconVisible = true,
                    isRightIconVisible = true
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn {
                    items(items = notes, key = { it.id }) { note ->
                        NoteItem(
                            note,
                            onDeleteBtnClicked = { vm.deleteNote(note) },
                            editNoteFun = {
                                navController.navigate("add_edit_notes_screen/${note.id}")
                            }
                        )
                    }
                }
            }

        }
    }
}


@Composable
private fun NoteItem(
    note: Note,
    editNoteFun: () -> Unit,
    onDeleteBtnClicked: () -> Unit
) {

    val context = LocalContext.current
    var isDelMessageVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val editNote = SwipeAction(
        onSwipe = {
            editNoteFun()
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

    val deleteNote = SwipeAction(
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
        startActions = listOf(editNote),
        endActions = listOf(deleteNote),
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
                        .padding(horizontal = 10.dp)
                        .padding(vertical = 10.dp)
                ) {

                    Text(
                        text = note.des,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 10.dp)
                    )

                    ConfirmMessage(
                        isMessageVisible = isDelMessageVisible,
                        onDeleteClick = {
                            onDeleteBtnClicked()
                            isDelMessageVisible = false
                            showToast(context, "Note deleted.", true)
                        },
                        onCancelClick = { isDelMessageVisible = false },
                        titleText = "Confirm Delete?",
                    )

                }
            }
        }
    }
}