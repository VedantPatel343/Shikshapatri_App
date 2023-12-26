package com.swaminarayan.shikshapatriApp.presentation.screens.homeScreen

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.swaminarayan.shikshapatriApp.R
import com.swaminarayan.shikshapatriApp.calender.CalenderDataSource
import com.swaminarayan.shikshapatriApp.calender.ui.Calender
import com.swaminarayan.shikshapatriApp.presentation.components.CircularImage
import com.swaminarayan.shikshapatriApp.presentation.components.Page
import com.swaminarayan.shikshapatriApp.utils.showToast
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    drawerState: DrawerState,
    vm: HomeViewModel = hiltViewModel()
) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val calenderData = CalenderDataSource()
    val visibleDateList = calenderData.getVisibleDates()

    Page(modifier = Modifier.padding(10.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    scope.launch { drawerState.open() }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Navigation Button"
                )
            }

            IconButton(
                onClick = {
                    scope.launch {
                        val id = vm.getIdByDate(LocalDate.now())
                        if (id != -1L) {
                            navController.navigate("single_day_report_screen/${id}")
                        } else {
                            showToast(context, "First fill today's form.", true)
                        }
                    }
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outlined_report_icon),
                    contentDescription = "Report Button"
                )
            }
        }

        Slogan()

        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularImage(image = R.drawable.maharaj1)
            CircularImage(image = R.drawable.guruji1)
        }

        Spacer(modifier = Modifier.height(10.dp))

        Calender(
            startEndDate = "Start NavDate - End date",
            visibleDateList = visibleDateList,
            onDateClicked = { date ->
                scope.launch {
                    val id = vm.getIdByDate(date)
                    if (id != -1L || date == LocalDate.now()) {
                        navController.navigate("daily_form_screen/${id}")
                    } else {
                        Toast.makeText(context, "Can not access it today.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            },
            onNextClick = { TODO("do something") },
            onPreviousClick = { },
            showPrevNextBtn = true
        )
    }
}

@Composable
fun Slogan() {

    Spacer(modifier = Modifier.height(5.dp))
    Divider(color = MaterialTheme.colorScheme.primary)
    Text(
        text = "Current Goal",
        fontSize = 18.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        textAlign = TextAlign.Center
    )
    Divider(color = MaterialTheme.colorScheme.primary)
    Spacer(modifier = Modifier.height(23.dp))

}
