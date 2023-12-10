package com.swaminarayan.shikshapatriApp.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BasicButton(
    modifier: Modifier = Modifier,
    text: String,
    onButtonClicked: () -> Unit,
    color: Color,
) {
    OutlinedButton(
        onClick = {
            onButtonClicked()
        },
        modifier = modifier,
        border = BorderStroke(1.dp, color)
    ) {
        Text(
            text = text,
            color = color
        )
    }
}