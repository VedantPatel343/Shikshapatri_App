package com.swaminarayan.shikshapatriApp.presentation.screens.aeNoteScreen

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.swaminarayan.shikshapatriApp.presentation.components.Page
import com.swaminarayan.shikshapatriApp.presentation.components.TopBar2Btn
import com.swaminarayan.shikshapatriApp.utils.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AENoteScreen(
    navController: NavHostController,
    noteId: Long,
    vm: AENotesViewModel = hiltViewModel()
) {

    val des by vm.des.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Page {
        TopBar2Btn(
            title = if (noteId == -1L) "Add Note" else "Edit Note",
            popBackStack = { navController.popBackStack() },
            onSaveClicked = {
                showToast(context, "Note saved.", true)
                vm.onSaveNote()
                navController.popBackStack()
            }
        )

        Spacer(modifier = Modifier.height(5.dp))

        BasicTextField(
            value = des,
            onValueChange = {
                vm.desChanged(it)
            },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize()
                ) {
                    if (des.isEmpty()) {
                        Text(text = "Start typing...", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(width = 8.dp))
                    innerTextField()
                    Spacer(modifier = Modifier.width(width = 8.dp))
                }
            }
        )


    }
}