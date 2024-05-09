package com.swaminarayan.shikshapatriApp

import android.os.Build
import android.os.Bundle
import android.util.Pair
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.swaminarayan.shikshapatriApp.constants.AGNA_ID
import com.swaminarayan.shikshapatriApp.constants.FORM_ID
import com.swaminarayan.shikshapatriApp.constants.NAV_DATE
import com.swaminarayan.shikshapatriApp.constants.NOTE_ID
import com.swaminarayan.shikshapatriApp.constants.gurujiLeftFaceList
import com.swaminarayan.shikshapatriApp.constants.gurujiList
import com.swaminarayan.shikshapatriApp.constants.gurujiRightFaceList
import com.swaminarayan.shikshapatriApp.constants.maharajList
import com.swaminarayan.shikshapatriApp.presentation.Screens
import com.swaminarayan.shikshapatriApp.presentation.SplashViewModel
import com.swaminarayan.shikshapatriApp.presentation.screens.aeAgnaScreen.AEAgnaScreen
import com.swaminarayan.shikshapatriApp.presentation.screens.aeNoteScreen.AENoteScreen
import com.swaminarayan.shikshapatriApp.presentation.screens.allAgnaScreen.AllAgnaScreen
import com.swaminarayan.shikshapatriApp.presentation.screens.dailyForm.DailyFormScreen
import com.swaminarayan.shikshapatriApp.presentation.screens.homeScreen.HomeScreen
import com.swaminarayan.shikshapatriApp.presentation.screens.noteScreen.NotesScreen
import com.swaminarayan.shikshapatriApp.presentation.screens.reportScreen.ReportScreen
import com.swaminarayan.shikshapatriApp.presentation.screens.singleDayReportScreen.SingleDayReportScreen
import com.swaminarayan.shikshapatriApp.ui.theme.ShikshapatriTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var splashViewModel: SplashViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition {
            splashViewModel.isLoading.value
        }

        setContent {
            val pair = setUpMaharajGuruji()
            val maharaj by rememberSaveable {
                mutableIntStateOf(pair.first)
            }
            val guruji by rememberSaveable {
                mutableIntStateOf(pair.second)
            }
            ShikshapatriTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    // remove notes section from home screen and add chesta, orda, etc, from youtube api.

                    Shikshapatri(maharaj, guruji)
                }
            }
        }
    }

    private fun setUpMaharajGuruji(): Pair<Int, Int> {
        val maharaj = maharajList.random()
        val guruji = when {
            maharaj.isLeftSideFace -> {
                gurujiRightFaceList.random().image
            }

            maharaj.isRightSideFace -> {
                gurujiLeftFaceList.random().image
            }

            else -> {
                gurujiList.random().image
            }
        }

        val maharajImage = maharaj.image

        return if (maharaj.isLeftSideFace) {
            Pair(guruji, maharajImage)
        } else {
            Pair(maharajImage, guruji)
        }
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Shikshapatri(maharaj: Int, guruji: Int) {

    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation(navController)
        }
    ) { paddingValues ->

        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = navController,
            startDestination = Screens.HomeScreen.route
        ) {
            composable(Screens.HomeScreen.route) {
                HomeScreen(navController, maharaj = maharaj, guruji = guruji)
            }

            composable(Screens.AllAgnaScreen.route) {
                AllAgnaScreen(navController)
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
                arguments = listOf(
                    navArgument(FORM_ID) { type = NavType.LongType },
                    navArgument(NAV_DATE) { type = NavType.StringType }
                )
            ) {
                DailyFormScreen(navController)
            }

            composable(Screens.ReportScreen.route) {
                ReportScreen(navController)
            }

            composable(Screens.NoteScreen.route) {
                NotesScreen(navController)
            }

            composable(
                Screens.SingleDayReportScreen.route,
                arguments = listOf(navArgument(FORM_ID) { type = NavType.LongType })
            ) {
                SingleDayReportScreen(navController)
            }

            composable(
                Screens.AENoteScreen.route,
                arguments = listOf(navArgument(NOTE_ID) { type = NavType.LongType })
            ) {
                val noteId = it.arguments?.getLong(NOTE_ID, -1L) ?: -1L
                AENoteScreen(navController, noteId)
            }

        }

    }

}

@Composable
fun BottomNavigation(navController: NavHostController) {

    val screens = listOf(
        Screens.AEAgnaScreen,
        Screens.AllAgnaScreen,
        Screens.HomeScreen,
        Screens.ReportScreen,
        Screens.NoteScreen
    )

    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route

    if (
        currentRoute != Screens.AEAgnaScreen.route &&
        currentRoute != Screens.AENoteScreen.route &&
        currentRoute != Screens.DailyFormScreen.route &&
        currentRoute != Screens.SingleDayReportScreen.route
    )
        NavigationBar {
            screens.forEach { screen ->
                val selected = currentRoute == screen.route
                NavigationBarItem(
                    selected = selected,
                    onClick = {
                        if (!selected) {
                            if (screen.route == Screens.AEAgnaScreen.route) {
                                navController.navigate("add_edit_agna_screen/${-1L}") {
                                    launchSingleTop = true
                                }
                            } else {
                                navController.navigate(screen.route) {
                                    launchSingleTop = true
                                    popUpTo(0)
                                }
                            }
                        }
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = if (selected) screen.filledIcon else screen.outlinedIcon),
                            contentDescription = null,
                            tint = if (selected) Color.White else Color.Black
                        )
                    },
                    label = {
                        Text(text = screen.title)
                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.primary)
                )
            }

        }
}
