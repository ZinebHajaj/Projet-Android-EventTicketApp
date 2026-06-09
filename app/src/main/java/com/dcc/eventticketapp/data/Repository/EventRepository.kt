package com.dcc.eventticketapp.data.Repository

import com.dcc.eventticketapp.data.Entities.EventModel
import javax.inject.Inject
import android.util.Log
import com.dcc.eventticketapp.data.Api.EventApi


class EventRepository @Inject constructor(
    private val api: EventApi
) {
    suspend fun getEvents(): List<EventModel> {
        val events = api.getEvents()
        Log.d("EventRepository", "size: " + events.size)
        return events
    }

    suspend fun getEventById(eventId: String): EventModel? {
        return getEvents().find { it.id == eventId }
    }
}