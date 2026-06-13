package com.dcc.eventticketapp.ui.home

sealed class HomeIntent {
    object LoadEvents     : HomeIntent()
    data class ToggleFavorite(val eventId: String) : HomeIntent()
    data class FilterByCategory(val category: String) : HomeIntent()
}