package com.dcc.eventticketapp.ui.events

sealed class EventsIntent {

    // Chargement
    object LoadEvents : EventsIntent()

    // Filtres
    data class SelectCategory(val category: String) : EventsIntent()
    data class SearchEvents(val query: String)       : EventsIntent()

    // Actions
    data class ToggleFavorite(val eventId: String)  : EventsIntent()

    // Reset
    object ResetState : EventsIntent()
}