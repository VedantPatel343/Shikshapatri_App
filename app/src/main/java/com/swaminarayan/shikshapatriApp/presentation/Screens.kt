package com.swaminarayan.shikshapatriApp.presentation

import com.swaminarayan.shikshapatriApp.R
import com.swaminarayan.shikshapatriApp.constants.AGNA_ID
import com.swaminarayan.shikshapatriApp.constants.GOAL_ID
import com.swaminarayan.shikshapatriApp.constants.NOTES_ID

sealed class Screens(
    val title: String,
    val filledIcon: Int,
    val outlinedIcon: Int,
    val route: String,
) {
    object HomeScreen: Screens(
        title = "Home Screen",
        filledIcon = R.drawable.maharaj1,
        outlinedIcon = R.drawable.maharaj1,
        route = "home_screen"
    )

    object AllAgnaScreen: Screens(
        title = "All Agnas",
        filledIcon = R.drawable.maharaj1,
        outlinedIcon = R.drawable.maharaj1,
        route = "all_agnas_screen"
    )

    object AEAgnaScreen: Screens(
        title = "Add Edit Agna",
        filledIcon = R.drawable.maharaj1,
        outlinedIcon = R.drawable.maharaj1,
        route = "add_edit_agna_screen/{$AGNA_ID}"
    )

    object ReportScreen: Screens(
        title = "Reports",
        filledIcon = R.drawable.maharaj1,
        outlinedIcon = R.drawable.maharaj1,
        route = "report_screen"
    )

    object GoalsScreen: Screens(
        title = "Goals",
        filledIcon = R.drawable.maharaj1,
        outlinedIcon = R.drawable.maharaj1,
        route = "goals_screen"
    )

    object AEGoalsScreen: Screens(
        title = "Add Edit Goal",
        filledIcon = 0,
        outlinedIcon = 0,
        route = "add_edit_goals_screen/{$GOAL_ID}"
    )

    object AgnaFormScreen: Screens(
        title = "Agna Form",
        filledIcon = 0,
        outlinedIcon = 0,
        route = "agna_form_screen"
    )


}
