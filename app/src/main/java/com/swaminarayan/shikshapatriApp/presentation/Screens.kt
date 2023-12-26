package com.swaminarayan.shikshapatriApp.presentation

import com.swaminarayan.shikshapatriApp.R
import com.swaminarayan.shikshapatriApp.constants.AGNA_ID
import com.swaminarayan.shikshapatriApp.constants.FORM_ID

sealed class Screens(
    val title: String,
    val filledIcon: Int,
    val outlinedIcon: Int,
    val route: String
) {
    object HomeScreen: Screens(
        title = "Home Screen",
        filledIcon = R.drawable.filled_home_icon,
        outlinedIcon = R.drawable.outlined_home_icon,
        route = "home_screen"
    )

    object AllAgnaScreen: Screens(
        title = "All Agnas",
        filledIcon = R.drawable.filled_all_agna_icon,
        outlinedIcon = R.drawable.outlined_all_agna_icon,
        route = "all_agnas_screen"
    )

    object AEAgnaScreen: Screens(
        title = "Add Agna",
        filledIcon = R.drawable.add_icon,
        outlinedIcon = R.drawable.add_icon,
        route = "add_edit_agna_screen/{$AGNA_ID}"
    )

    object ReportScreen: Screens(
        title = "Reports",
        filledIcon = R.drawable.filled_report_icon,
        outlinedIcon = R.drawable.outlined_report_icon,
        route = "report_screen"
    )

    object SingleDayReportScreen: Screens(
        title = "Daily Report",
        filledIcon = 0,
        outlinedIcon = 0,
        route = "single_day_report_screen/{$FORM_ID}"
    )

    object DailyFormScreen: Screens(
        title = "Agna Form",
        filledIcon = 0,
        outlinedIcon = 0,
        route = "daily_form_screen/{$FORM_ID}"
    )


}
