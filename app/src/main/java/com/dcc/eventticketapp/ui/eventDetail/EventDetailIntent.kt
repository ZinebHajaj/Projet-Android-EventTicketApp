package com.dcc.eventticketapp.ui.eventDetail

sealed class EventDetailIntent {
    data class LoadEvent(val eventId: String) : EventDetailIntent()
    object ToggleFavorite                     : EventDetailIntent()
    object BookEvent                          : EventDetailIntent()
}