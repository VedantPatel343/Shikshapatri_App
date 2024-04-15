package com.swaminarayan.shikshapatriApp.presentation.screens.aeAgnaScreen

sealed class AEAgnaEvents {

    data class OnAgnaChange(val agna: String) : AEAgnaEvents()
    data class OnDesChange(val des: String) : AEAgnaEvents()
    data class OnAuthorChange(val author: String) : AEAgnaEvents()
    data class OnPointsChange(val points: String) : AEAgnaEvents()
    data class IsAlwaysPalayCheChange(val alwaysPalayChe: Boolean) : AEAgnaEvents()
    data class IsCounterChange(val isCounter: Boolean) : AEAgnaEvents()


    data object OnSaveAgna : AEAgnaEvents()

}