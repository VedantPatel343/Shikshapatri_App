package com.swaminarayan.shikshapatriApp.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ConfirmMessage(
    isMessageVisible: Boolean,
    onButtonClick: () -> Unit,
    titleText: String,
    buttonText: String,
    bgColor: Color = MaterialTheme.colorScheme.secondary
) {

    AnimatedVisibility(visible = isMessageVisible) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 1.dp,
                    shape = RoundedCornerShape(5.dp)
                )
                .background(bgColor)
        ) {
            Text(text = titleText, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 5.dp))
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = 5.dp,
                        horizontal = 5.dp
                    ),
                onClick = {
                    onButtonClick()
                },
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
            ) {
                Text(text = buttonText, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(3.dp))
        }
    }

}