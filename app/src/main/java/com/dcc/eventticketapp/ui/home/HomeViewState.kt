package com.dcc.eventticketapp.ui.home

import com.dcc.eventticketapp.data.Entities.EventModel

data class HomeViewState(
    val isLoading      : Boolean          = false,
    val upcomingEvents : List<EventModel> = emptyList(),
    val allEvents        : List<EventModel> = emptyList(),
    //val selectedCategory : String           = "Tous",
    val error          : String?          = null
)