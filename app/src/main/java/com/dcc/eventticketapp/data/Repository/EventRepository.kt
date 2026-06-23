package com.dcc.eventticketapp.data.Repository

import android.net.Uri
import android.util.Log
import com.dcc.eventticketapp.data.Api.EventApi
import com.dcc.eventticketapp.data.Database.EventDao
import com.dcc.eventticketapp.data.Entities.EventModel
import com.dcc.eventticketapp.data.Entities.SeatTier
import com.dcc.eventticketapp.data.Entities.TimeSlot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val api: EventApi,
    private val eventDao: EventDao,
    private val ticketRepository: TicketRepository,
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    // ========== LECTURE UNIQUEMENT DEPUIS FIRESTORE ==========

    suspend fun getEvents(): List<EventModel> {
        return try {
            // 1. VIDER Room pour éviter les vieux événements
            eventDao.deleteAll()

            // 2. Lire depuis Firestore
            val snapshot = firestore
                .collection("events")
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val events = snapshot.documents.mapNotNull { doc ->
                doc.toObject(EventModel::class.java)
            }

            Log.d("EventRepository", "Firestore events: ${events.size}")

            // 3. Mettre en cache dans Room
            eventDao.insertAll(events)

            events
        } catch (e: Exception) {
            Log.e("EventRepository", "Firestore failed: ${e.message}")
            eventDao.getAll()
        }
    }

    suspend fun getEventById(eventId: String): EventModel? {
        // 1. Essayer Room d'abord
        val cached = eventDao.getEventById(eventId)
        if (cached != null) return cached

        // 2. Essayer Firestore
        return try {
            val doc = firestore
                .collection("events")
                .document(eventId)
                .get()
                .await()

            doc.toObject(EventModel::class.java)?.also {
                eventDao.insert(it)  // Mettre en cache
            }
        } catch (e: Exception) {
            null
        }
    }

    // ── Génère les 3 catégories de sièges à partir du prix de base ──
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
                bookedSeats = total - available,
                benefits = if (tierId == "vip")
                    listOf("Accès coupe-file", "Boissons offertes", "Zone premium")
                else
                    emptyList()
            )
        }
    }

    // ── Génère des créneaux pour Ateliers/Autres ─────────────────────
    fun generateTimeSlots(): List<TimeSlot> {
        return listOf(
            TimeSlot(slotId = "slot1", time = "10:00", totalCapacity = 30, booked = 18),
            TimeSlot(slotId = "slot2", time = "14:00", totalCapacity = 30, booked = 5),
            TimeSlot(slotId = "slot3", time = "17:00", totalCapacity = 30, booked = 0)
        )
    }

    // ========== CRUD POUR ORGANISATEUR ==========

    suspend fun createEvent(event: EventModel): Boolean {
        return try {
            // 1. Sauvegarder dans Firestore
            firestore.collection("events")
                .document(event.id)
                .set(event)
                .await()

            // 2. Mettre en cache Room
            eventDao.insert(event)

            // 3. Initialiser les compteurs de places
            initializeSeatCounters(event.id, event.priceStandard)

            Log.d("EventRepository", "Event created: ${event.id}")
            true
        } catch (e: Exception) {
            Log.e("EventRepository", "Create event failed: ${e.message}", e)
            false
        }
    }

    suspend fun updateEvent(event: EventModel): Boolean {
        return try {
            val updated = event.copy(updatedAt = System.currentTimeMillis())

            firestore.collection("events")
                .document(event.id)
                .set(updated)
                .await()

            eventDao.insert(updated)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteEvent(eventId: String): Boolean {
        return try {
            // 1. Supprimer les sous-collections
            val tiersSnapshot = firestore
                .collection("events")
                .document(eventId)
                .collection("seatTiers")
                .get()
                .await()

            for (tier in tiersSnapshot.documents) {
                tier.reference.delete().await()
            }

            // 2. Supprimer le document principal
            firestore.collection("events")
                .document(eventId)
                .delete()
                .await()

            // 3. Supprimer de Room
            eventDao.deleteById(eventId)

            Log.d("EventRepository", "Event deleted: $eventId")
            true
        } catch (e: Exception) {
            Log.e("EventRepository", "Delete failed: ${e.message}")
            false
        }
    }

    suspend fun getMyEvents(organizerId: String): List<EventModel> {
        return try {
            val snapshot = firestore
                .collection("events")
                .whereEqualTo("organizerId", organizerId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull {
                it.toObject(EventModel::class.java)
            }
        } catch (e: Exception) {
            eventDao.getMyEvents(organizerId)
        }
    }

    // Helper
    private suspend fun initializeSeatCounters(eventId: String, basePrice: Double) {
        val tiers = mapOf(
            "vip" to 50,
            "standard" to 200,
            "economy" to 300
        )

        tiers.forEach { (tierId, total) ->
            firestore.collection("events")
                .document(eventId)
                .collection("seatTiers")
                .document(tierId)
                .set(mapOf("totalSeats" to total, "bookedSeats" to 0))
                .await()
        }
    }

    suspend fun uploadEventImage(uri: Uri, eventId: String): String {
        val imageRef = storage.reference.child("events/$eventId/${UUID.randomUUID()}.jpg")
        imageRef.putFile(uri).await()
        return imageRef.downloadUrl.await().toString()
    }
}