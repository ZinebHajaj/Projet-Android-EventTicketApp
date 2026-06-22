package com.dcc.eventticketapp.data.Repository

import android.util.Log
import com.dcc.eventticketapp.data.Api.EventApi
import com.dcc.eventticketapp.data.Database.EventDao
import com.dcc.eventticketapp.data.Entities.EventModel
import com.dcc.eventticketapp.data.Entities.SeatTier
import com.dcc.eventticketapp.data.Entities.TimeSlot
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val api      : EventApi,
    private val eventDao : EventDao,
    private val ticketRepository: TicketRepository
) {

    suspend fun getEvents(): List<EventModel> {
        try {
            val events = api.getEvents()
            Log.d("EventRepository", "Network size = ${events.size}")
            eventDao.insertAll(events)
        } catch (e: Exception) {
            Log.e("EventRepository", "Using cache", e)
        }
        return eventDao.getAll()
    }

    suspend fun getEventById(eventId: String): EventModel? {
        return eventDao.getEventById(eventId)
    }

    // ── Génère les 3 catégories de sièges à partir du prix de base ──
    // REMPLACER la fonction generateSeatTiers existante par :

    suspend fun generateSeatTiers(eventId: String, basePrice: Double): List<SeatTier> {
        val tiers = listOf(
            Triple("vip", "VIP", basePrice * 1.5),
            Triple("standard", "Standard", basePrice),
            Triple("economy", "Économique", basePrice * 0.65)
        )

        return tiers.map { (tierId, name, price) ->
            val available = ticketRepository.getAvailableSeats(eventId, tierId)
            val total = when (tierId) {
                "vip" -> 50
                "standard" -> 200
                "economy" -> 300
                else -> 100
            }

            SeatTier(
                tierId = tierId,
                name = name,
                price = price,
                totalSeats = total,
                bookedSeats = total - available,  // ← Calculé depuis Firestore
                benefits = if (tierId == "vip")
                    listOf("Accès coupe-file", "Boissons offertes", "Zone premium")
                else
                    emptyList()
            )
        }
    }

    /*fun generateSeatTiers(basePrice: Double): List<SeatTier> {
        return listOf(
            SeatTier(
                tierId      = "vip",
                name        = "VIP",
                price       = basePrice * 1.5,
                totalSeats  = 50,
                bookedSeats = 12,
                benefits    = listOf("Accès coupe-file", "Boissons offertes", "Zone premium")
            ),
            SeatTier(
                tierId      = "standard",
                name        = "Standard",
                price       = basePrice,
                totalSeats  = 200,
                bookedSeats = 80,
                benefits    = emptyList()
            ),
            SeatTier(
                tierId      = "economy",
                name        = "Économique",
                price       = basePrice * 0.65,
                totalSeats  = 300,
                bookedSeats = 45,
                benefits    = emptyList()
            )
        )
    }*/

    // ── Génère des créneaux pour Ateliers/Autres ─────────────────────
    fun generateTimeSlots(): List<TimeSlot> {
        return listOf(
            TimeSlot(slotId = "slot1", time = "10:00", totalCapacity = 30, booked = 18),
            TimeSlot(slotId = "slot2", time = "14:00", totalCapacity = 30, booked = 5),
            TimeSlot(slotId = "slot3", time = "17:00", totalCapacity = 30, booked = 0)
        )
    }
}



/*
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
*/