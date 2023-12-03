package com.swaminarayan.shikshapatriApp.presentation

import com.swaminarayan.shikshapatriApp.R
import com.swaminarayan.shikshapatriApp.constants.AGNAID

sealed class Screens(
    val title: String,
    val route: String,
    val filledIcon: Int,
    val outlinedIcon: Int,
) {
    object HomeScreen: Screens(
        title = "Home Screen",
        route = "home_screen",
        filledIcon = R.drawable.maharaj1,
        outlinedIcon = R.drawable.maharaj1
    )

    object AllAgnaScreen: Screens(
        title = "All Agnas",
        route = "all_agnas_screen",
        filledIcon = R.drawable.maharaj1,
        outlinedIcon = R.drawable.maharaj1
    )

    object AEAgnaScreen: Screens(
        title = "Add Edit Agna",
        route = "add_edit_agna_screen/{$AGNAID}",
        filledIcon = R.drawable.maharaj1,
        outlinedIcon = R.drawable.maharaj1
    )

    object ReportScreen: Screens(
        title = "Reports",
        route = "report_screen",
        filledIcon = R.drawable.maharaj1,
        outlinedIcon = R.drawable.maharaj1
    )

    object NotesScreen: Screens(
        title = "Notes",
        route = "notes_screen",
        filledIcon = R.drawable.maharaj1,
        outlinedIcon = R.drawable.maharaj1
    )

    object AENotesScreen: Screens(
        title = "Add Edit Note",
        route = "add_edit_notes_screen",
        filledIcon = 0,
        outlinedIcon = 0
    )

    object GoalsScreen: Screens(
        title = "Goals",
        route = "goals_screen",
        filledIcon = R.drawable.maharaj1,
        outlinedIcon = R.drawable.maharaj1
    )

    object AEGoalsScreen: Screens(
        title = "Add Edit Goal",
        route = "add_edit_goals_screen",
        filledIcon = 0,
        outlinedIcon = 0
    )


}
