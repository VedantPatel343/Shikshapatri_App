package com.swaminarayan.shikshapatriApp.presentation.screens.aeAgnaScreen

sealed class AEAgnaEvents {

    data class OnAgnaChange(val agna: String): AEAgnaEvents()
    data class OnDesChange(val des: String): AEAgnaEvents()
    data class OnAuthorChange(val author: String): AEAgnaEvents()
    data class OnSlokNoChange(val slokNo: String): AEAgnaEvents()
    data class OnPointsChange(val points: String): AEAgnaEvents()
    data class OnAlwaysPalayCheChange(val alwaysPalayChe: Boolean): AEAgnaEvents()


    data object OnSaveAgna: AEAgnaEvents()

}