package com.swaminarayan.shikshapatriApp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerMenu(drawerState: DrawerState, navController: NavHostController) {

    val scope = rememberCoroutineScope()

    val screens = listOf(
        Screens.HomeScreen,
        Screens.AllAgnaScreen,
        Screens.AEAgnaScreen,
        Screens.ReportScreen,
        Screens.NoteScreen
    )

    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route


    Column(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 5.dp)
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        screens.forEach { screen ->
            val flag = currentRoute == screen.route
            DrawerMenuItem(
                filledIcon = screen.filledIcon,
                outlinedIcon = screen.outlinedIcon,
                title = screen.title,
                selected = flag,
                onClick = {
                    scope.launch {
                        drawerState.close()
                    }
                    if(screen.route == Screens.AEAgnaScreen.route) {
                        navController.navigate("add_edit_agna_screen/${-1L}") {
                            launchSingleTop = true
                        }
                    } else {
                        navController.navigate(screen.route) {
                            launchSingleTop = true
                            popUpTo(Screens.HomeScreen.route)
                        }
                    }
                }
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerMenuItem(
    filledIcon: Int,
    outlinedIcon: Int,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    NavigationDrawerItem(
        label = {
            Text(
                text = title,
                Modifier.padding(start = 10.dp),
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        },
        selected = selected,
        onClick = { onClick() },
        icon = {
            Icon(
                painter = painterResource(id = if (selected) filledIcon else outlinedIcon),
                contentDescription = title,
                Modifier.size(25.dp)
            )
        },
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedIconColor = MaterialTheme.colorScheme.background,
            selectedTextColor = MaterialTheme.colorScheme.background,
            unselectedContainerColor = MaterialTheme.colorScheme.background,
        ),
        modifier = Modifier.padding(bottom = 10.dp)
    )

}
