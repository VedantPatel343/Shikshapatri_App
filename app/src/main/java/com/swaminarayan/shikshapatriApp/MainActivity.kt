package com.swaminarayan.shikshapatriApp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.swaminarayan.shikshapatriApp.constants.AGNA_ID
import com.swaminarayan.shikshapatriApp.constants.FORM_ID
import com.swaminarayan.shikshapatriApp.presentation.DrawerMenu
import com.swaminarayan.shikshapatriApp.presentation.Screens
import com.swaminarayan.shikshapatriApp.presentation.components.CircularImage
import com.swaminarayan.shikshapatriApp.presentation.screens.aeAgnaScreen.AEAgnaScreen
import com.swaminarayan.shikshapatriApp.presentation.screens.allAgnaScreen.AllAgnaScreen
import com.swaminarayan.shikshapatriApp.presentation.screens.dailyForm.DailyFormScreen
import com.swaminarayan.shikshapatriApp.presentation.screens.dailyReportScreen.SingleDayReportScreen
import com.swaminarayan.shikshapatriApp.presentation.screens.homeScreen.HomeScreen
import com.swaminarayan.shikshapatriApp.presentation.screens.reportScreen.ReportScreen
import com.swaminarayan.shikshapatriApp.ui.theme.ShikshapatriTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShikshapatriTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Shikshapatri()
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Shikshapatri() {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val navController = rememberNavController()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            ModalDrawerSheet {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(MaterialTheme.colorScheme.primary),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    CircularImage(image = R.drawable.maharaj1, size = 120.dp)
                    Text(
                        text = "Shikshapatri",
                        color = MaterialTheme.colorScheme.background,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                }

                DrawerMenu(
                    drawerState = drawerState,
                    navController = navController
                )
            }
        }
    ) {

        NavHost(
            navController = navController,
            startDestination = Screens.HomeScreen.route
        ) {
            composable(Screens.HomeScreen.route) {
                HomeScreen(navController, drawerState)
            }

            composable(Screens.AllAgnaScreen.route) {
                AllAgnaScreen(drawerState, navController)
            }

            composable(
                Screens.AEAgnaScreen.route,
                arguments = listOf(navArgument(AGNA_ID) { type = NavType.LongType })
            ) {
                val agnaId = it.arguments?.getLong(AGNA_ID, -1L) ?: -1L
                AEAgnaScreen(agnaId, navController)
            }

            composable(
                Screens.DailyFormScreen.route,
                arguments = listOf(navArgument(FORM_ID) { type = NavType.LongType })
            ) {
                DailyFormScreen(navController)
            }

            composable(Screens.ReportScreen.route) {
                ReportScreen(drawerState, navController)
            }

            composable(
                Screens.SingleDayReportScreen.route,
                arguments = listOf(navArgument(FORM_ID) { type = NavType.LongType })
            ) {
                SingleDayReportScreen(navController)
            }

        }

    }

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShikshapatriTheme {
        Shikshapatri()
    }
}