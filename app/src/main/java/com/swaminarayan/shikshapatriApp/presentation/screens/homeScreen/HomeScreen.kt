package com.swaminarayan.shikshapatriApp.presentation.screens.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.swaminarayan.shikshapatriApp.R
import com.swaminarayan.shikshapatriApp.presentation.components.CircularImage

@Composable
fun HomeScreen() {

    Column(modifier = Modifier.fillMaxSize()) {

        Row(Modifier.fillMaxWidth()) {
            CircularImage(image = R.drawable.maharaj1)
            Image(imageVector = Icons.Default.TextFields, contentDescription = "Shikshapatri")
            CircularImage(image = R.drawable.maharaj1)
        }

        val list = listOf(1, 2, 3)

        LazyColumn {
            item {
                Column(Modifier.fillMaxWidth()) {
                    Text(text = "Current Goal", fontSize = 28.sp)

                    // Add Weekly calender here.
                }
            }

            items(list) { agna ->
                AgnaItem()
            }
        }


    }

}

@Composable
private fun AgnaItem() {

}
