package com.swaminarayan.shikshapatriApp.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.swaminarayan.shikshapatriApp.ui.theme.Red

@Composable
fun ConfirmMessage(
    isMessageVisible: Boolean,
    onDeleteClick: () -> Unit,
    onCancelClick: () -> Unit,
    titleText: String
) {

    AnimatedVisibility(visible = isMessageVisible) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = titleText, color = Red, modifier = Modifier.padding(vertical = 5.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp),
                    onClick = {
                        onCancelClick()
                    },
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Text(text = "Cancel", color = Color.Gray)
                    Spacer(modifier = Modifier.height(3.dp))
                }

                OutlinedButton(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 10.dp),
                    onClick = {
                        onDeleteClick()
                    },
                    border = BorderStroke(1.dp, Red)
                ) {
                    Text(text = "Delete", color = Red)
                    Spacer(modifier = Modifier.height(3.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

    }
}