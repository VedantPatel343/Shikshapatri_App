package com.swaminarayan.shikshapatriApp.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OTFWithError(
    text: String,
    onTextChanged: (String) -> Unit,
    onClearTextClicked: () -> Unit,
    label: String,
    isError: Boolean,
    keyBoardType: KeyboardType,
    modifier: Modifier = Modifier
) {

    // *******************************************************************

    Column {
        OutlinedTextField(
            modifier = modifier,
            value = text,
            onValueChange = {
                onTextChanged(it)
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = keyBoardType,
                capitalization = KeyboardCapitalization.Sentences
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
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            },
            singleLine = true,
            label = { Text(text = label) },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = MaterialTheme.colorScheme.onSecondary,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                errorBorderColor = MaterialTheme.colorScheme.error,
                errorCursorColor = MaterialTheme.colorScheme.error,
                errorLabelColor = MaterialTheme.colorScheme.error,
                unfocusedLabelColor = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}