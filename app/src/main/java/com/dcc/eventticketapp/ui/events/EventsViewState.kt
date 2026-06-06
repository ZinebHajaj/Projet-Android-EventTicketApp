package com.dcc.eventticketapp.ui.events

import com.dcc.eventticketapp.data.Entities.EventModel

data class EventsViewState(

    // État général
    val isLoading        : Boolean          = false,
    val error            : String?          = null,

    // Données
    val allEvents        : List<EventModel> = emptyList(),
    val filteredEvents   : List<EventModel> = emptyList(),

    // Filtres
    val selectedCategory : String           = "Tous",
    val searchQuery      : String           = "",

    // Catégories disponibles
    val categories       : List<String>     = listOf(
        "Tous", "Concerts", "Sports", "Théâtre", "Ateliers", "Autres"
    )
)