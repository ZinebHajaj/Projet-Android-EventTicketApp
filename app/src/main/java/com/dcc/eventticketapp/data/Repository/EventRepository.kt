package com.dcc.eventticketapp.data.Repository

import android.util.Log
import com.dcc.eventticketapp.data.Api.EventApi
import com.dcc.eventticketapp.data.Database.EventDao
import com.dcc.eventticketapp.data.Entities.EventModel
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val api: EventApi,
    private val eventDao: EventDao
) {

    suspend fun getEvents(): List<EventModel> {

        try {

            val events = api.getEvents()

            Log.d(
                "EventRepository",
                "Network size = ${events.size}"
            )

            eventDao.insertAll(events)

        } catch (e: Exception) {

            Log.e(
                "EventRepository",
                "Using cache",
                e
            )
        }

        return eventDao.getAll()
    }

    suspend fun getEventById(
        eventId: String
    ): EventModel? {

        return eventDao.getEventById(eventId)
    }
}