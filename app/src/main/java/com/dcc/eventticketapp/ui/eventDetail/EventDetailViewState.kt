package com.dcc.eventticketapp.ui.eventDetail

import com.dcc.eventticketapp.data.Entities.EventModel

data class EventDetailViewState(
    val isLoading : Boolean     = false,
    val event     : EventModel? = null,
    val error     : String?     = null,
    val isBooked  : Boolean     = false
)