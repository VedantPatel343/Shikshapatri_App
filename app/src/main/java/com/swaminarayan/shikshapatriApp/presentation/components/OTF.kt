package com.swaminarayan.shikshapatriApp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun OTF(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    onClearTextClicked: () -> Unit,
    label: String,
    isError: Boolean,
    keyBoardType: KeyboardType,
    imeAction: ImeAction = ImeAction.Next
) {

    val localFocus = LocalFocusManager.current
    val keyBoardAction = if (imeAction == ImeAction.Next) {
        KeyboardActions(onNext = { localFocus.moveFocus(FocusDirection.Down) })
    } else {
        KeyboardActions(onDone = { localFocus.clearFocus() })
    }

    Column {
        OutlinedTextField(
            modifier = modifier,
            value = text,
            onValueChange = {
                onTextChanged(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyBoardType,
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = imeAction
            ),
            maxLines = 1,
            isError = isError,
            trailingIcon = {
                if (text.isNotEmpty()) {
                    Icon(
                        modifier = Modifier
                            .clickable { onClearTextClicked() }
                            .padding(end = 3.dp)
                            .size(20.dp),
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear"
                    )
                }
            },
            singleLine = true,
            keyboardActions = keyBoardAction,
            label = { Text(text = label) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                errorBorderColor = Color.Red,
                errorCursorColor = Color.Red,
                errorLabelColor = Color.Red
            )
        )
    }
}