package com.swaminarayan.shikshapatriApp.presentation.screens.noteScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.swaminarayan.shikshapatriApp.R
import com.swaminarayan.shikshapatriApp.domain.models.Note
import com.swaminarayan.shikshapatriApp.presentation.components.ConfirmMessage
import com.swaminarayan.shikshapatriApp.presentation.components.Page
import com.swaminarayan.shikshapatriApp.ui.theme.Red
import com.swaminarayan.shikshapatriApp.utils.showToast
import kotlinx.coroutines.launch
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    drawerState: DrawerState,
    navController: NavHostController,
    vm: NotesViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()
    val notes by vm.notes.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            IconButton(
                onClick = { navController.navigate("add_edit_notes_screen/${-1L}") },
                modifier = Modifier
                    .clip(CircleShape)
                    .size(60.dp),
                colors = IconButtonDefaults.iconButtonColors(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
    ) { paddingValue ->

        Page(modifier = Modifier.padding(paddingValue)) {

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 15.dp)
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = {
                        scope.launch { drawerState.open() }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Navigation Button"
                    )
                }

                Text(
                    text = "Notes",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = FontFamily.Cursive
                )

                Icon(
                    painter = painterResource(id = R.drawable.outlined_report_icon),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.background,
                    modifier = Modifier.padding(end = 15.dp)
                )
            }

            if (notes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No notes added.")
                }
            } else {
                Spacer(modifier = Modifier.height(15.dp))
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
        modifier = Modifier.padding(bottom = 10.dp)
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
                        fontSize = 18.sp,
                        modifier = Modifier
                            .padding(bottom = 5.dp)
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