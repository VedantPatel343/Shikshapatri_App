package com.swaminarayan.shikshapatriApp.presentation.screens.homeScreen

sealed class HomeEvent {

    data object OnNextDateClicked : HomeEvent()
    data object OnPrevDateClicked : HomeEvent()
    data object ChangeFlagValue : HomeEvent()
    data class UpdateSlogan(val slogan: String) : HomeEvent()

}