package com.dcc.eventticketapp.ui.favorites
import com.dcc.eventticketapp.data.Entities.EventModel

data class FavoritesViewState(
    val isLoading        : Boolean          = false,
    val error            : String?          = null,
    val allFavorites     : List<EventModel> = emptyList(),
    val filteredFavorites: List<EventModel> = emptyList(),
    val selectedFilter   : String           = "Tous",
    val filters          : List<String>     = listOf(
        "Tous", "Concerts", "Sports", "Théâtre", "Ateliers"
    ),
    val showClearDialog  : Boolean          = false
)