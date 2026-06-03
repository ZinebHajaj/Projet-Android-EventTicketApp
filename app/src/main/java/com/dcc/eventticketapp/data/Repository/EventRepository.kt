package com.dcc.eventticketapp.data.Repository

import com.dcc.eventticketapp.data.Entities.EventModel
import kotlinx.coroutines.delay
import javax.inject.Inject

class EventRepository @Inject constructor() {
    suspend fun getEvents(): List<EventModel> {
        delay(1000)
        return listOf(
            EventModel(
                id            = "1",
                title         = "Concert Jazz Night",
                city          = "Casablanca",
                date          = "15 Juin 2025",
                imageUrl      = "https://picsum.photos/seed/jazz/400/300",
                priceStandard = 150.0,

                category = "Concerts"
            ),
            EventModel(
                id            = "2",
                title         = "Festival Gnaoua",
                city          = "Essaouira",
                date          = "20 Juin 2025",
                imageUrl      = "https://picsum.photos/seed/gnaoua/400/300",
                priceStandard = 200.0
            ),
            EventModel(
                id            = "3",
                title         = "Match Raja vs Wydad",
                city          = "Casablanca",
                date          = "25 Juin 2025",
                imageUrl      = "https://picsum.photos/seed/football/400/300",
                priceStandard = 100.0,

                category = "Sports"
            ),
            EventModel(
                id            = "4",
                title         = "Théâtre Mohamed V",
                city          = "Rabat",
                date          = "30 Juin 2025",
                imageUrl      = "https://picsum.photos/seed/theatre/400/300",
                priceStandard = 80.0,

                category = "Théâtre"
            )
        )
    }

    suspend fun getEventById(eventId: String): EventModel? {
        delay(500)
        return getEvents().find { it.id == eventId }
    }

}