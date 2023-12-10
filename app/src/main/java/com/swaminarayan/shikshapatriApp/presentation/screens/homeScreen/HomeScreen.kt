package com.swaminarayan.shikshapatriApp.presentation.screens.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.swaminarayan.shikshapatriApp.R
import com.swaminarayan.shikshapatriApp.constants.AGNA_ID
import com.swaminarayan.shikshapatriApp.presentation.Screens
import com.swaminarayan.shikshapatriApp.presentation.components.CircularImage
import com.swaminarayan.shikshapatriApp.presentation.components.Page
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, drawerState: DrawerState) {

    val scope = rememberCoroutineScope()

    Page {

        IconButton(onClick = {
            scope.launch { drawerState.open() }
        }) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = null)
        }

        Row(
            Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(Screens.AllAgnaScreen.route) },
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularImage(image = R.drawable.maharaj1)
            Image(imageVector = Icons.Default.TextFields, contentDescription = "Shikshapatri")
            CircularImage(image = R.drawable.guruji1)
        }

        Text(text = "Current Goal", fontSize = 28.sp)

        // Add Weekly calender here.


    }

}

@Composable
fun Calender() {

}