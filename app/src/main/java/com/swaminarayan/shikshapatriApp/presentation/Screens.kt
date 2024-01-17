package com.swaminarayan.shikshapatriApp.presentation

import com.swaminarayan.shikshapatriApp.R
import com.swaminarayan.shikshapatriApp.constants.AGNA_ID
import com.swaminarayan.shikshapatriApp.constants.FORM_ID
import com.swaminarayan.shikshapatriApp.constants.NAV_DATE
import com.swaminarayan.shikshapatriApp.constants.NOTE_ID

sealed class Screens(
    val title: String,
    val filledIcon: Int,
    val outlinedIcon: Int,
    val route: String
) {
    data object HomeScreen: Screens(
        title = "Home Screen",
        filledIcon = R.drawable.filled_home_icon,
        outlinedIcon = R.drawable.outlined_home_icon,
        route = "home_screen"
    )

    data object AllAgnaScreen: Screens(
        title = "All Agnas",
        filledIcon = R.drawable.filled_all_agna_icon,
        outlinedIcon = R.drawable.outlined_all_agna_icon,
        route = "all_agnas_screen"
    )

    data object AEAgnaScreen: Screens(
        title = "Add Agna",
        filledIcon = R.drawable.add_icon,
        outlinedIcon = R.drawable.add_icon,
        route = "add_edit_agna_screen/{$AGNA_ID}"
    )

    data object ReportScreen: Screens(
        title = "Reports",
        filledIcon = R.drawable.filled_report_icon,
        outlinedIcon = R.drawable.outlined_report_icon,
        route = "report_screen"
    )

    data object SingleDayReportScreen: Screens(
        title = "Daily Report",
        filledIcon = 0,
        outlinedIcon = 0,
        route = "single_day_report_screen/{$FORM_ID}"
    )

    data object DailyFormScreen: Screens(
        title = "Agna Form",
        filledIcon = 0,
        outlinedIcon = 0,
        route = "daily_form_screen/{$FORM_ID}/{$NAV_DATE}"
    )

    data object NoteScreen: Screens(
        title = "Notes",
        filledIcon = R.drawable.filled_notes_icon,
        outlinedIcon = R.drawable.outline_notes_icon,
        route = "notes_screen"
    )

    data object AENoteScreen: Screens(
        title = "Add/Edit Notes",
        filledIcon = 0,
        outlinedIcon = 0,
        route = "add_edit_notes_screen/{$NOTE_ID}"
    )

}
