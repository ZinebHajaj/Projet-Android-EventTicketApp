package com.dcc.eventticketapp.data.Repository

import com.dcc.eventticketapp.data.Entities.TicketModel
import com.dcc.eventticketapp.data.Entities.TicketStatus
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class TicketRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    // Sauvegarder un ticket dans Firestore
    suspend fun saveTicket(ticket: TicketModel, userId: String) {
        firestore
            .collection("users")
            .document(userId)
            .collection("tickets")
            .document(ticket.ticketId)
            .set(ticket)
            .await()
    }

    // Charger les tickets depuis Firestore
    suspend fun getTickets(userId: String): List<TicketModel> {
        val snapshot = firestore
            .collection("users")
            .document(userId)
            .collection("tickets")
            .get()
            .await()

        return snapshot.documents.mapNotNull {
            it.toObject(TicketModel::class.java)
        }
    }

    suspend fun getAvailableSeats(eventId: String, tierId: String): Int {
        return try {
            val doc = firestore
                .collection("events")
                .document(eventId)
                .collection("seatTiers")
                .document(tierId)
                .get()
                .await()

            val total = doc.getLong("totalSeats")?.toInt() ?: getDefaultTotal(tierId)
            val booked = doc.getLong("bookedSeats")?.toInt() ?: 0

            total - booked
        } catch (e: Exception) {
            // Si le document n'existe pas du tout, retourner le total par défaut
            getDefaultTotal(tierId)
        }
    }

    // Incrémenter le compteur de places réservées
    suspend fun incrementBookedSeats(eventId: String, tierId: String, quantity: Int): Boolean {
        return try {
            val counterRef = firestore
                .collection("events")
                .document(eventId)
                .collection("seatTiers")
                .document(tierId)

            val snapshot = counterRef.get().await()

            if (snapshot.exists()) {
                // Document existe, incrémenter
                val currentBooked = snapshot.getLong("bookedSeats")?.toInt() ?: 0
                counterRef.update("bookedSeats", currentBooked + quantity).await()
            } else {
                // Document n'existe pas, le créer
                val total = getDefaultTotal(tierId)
                counterRef.set(mapOf(
                    "totalSeats" to total,
                    "bookedSeats" to quantity
                )).await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    // Helper pour les valeurs par défaut
    private fun getDefaultTotal(tierId: String): Int {
        return when (tierId) {
            "vip" -> 50
            "standard" -> 200
            "economy" -> 300
            else -> 100
        }
    }
}

/*
import com.dcc.eventticketapp.data.Entities.TicketModel
import com.dcc.eventticketapp.data.Entities.TicketStatus
import kotlinx.coroutines.delay
import javax.inject.Inject

class TicketRepository @Inject constructor() {
    suspend fun getTickets(): List<TicketModel> {
        delay(1000)
        return listOf(
            TicketModel(
                ticketId   = "TK001",
                eventTitle = "Concert Jazz Night",
                eventDate  = "15 Juin 2025",
                eventCity  = "Casablanca",
                eventImage = "https://picsum.photos/seed/jazz/400/300",
                category   = "Concerts",
                price      = 150.0,
                qrCode     = "TICKET-TK001-JAZZ-CASABLANCA-2025",
                status     = TicketStatus.VALID
            ),
            TicketModel(
                ticketId   = "TK002",
                eventTitle = "Match Raja vs Wydad",
                eventDate  = "25 Juin 2025",
                eventCity  = "Casablanca",
                eventImage = "https://picsum.photos/seed/football/400/300",
                category   = "Sports",
                price      = 100.0,
                qrCode     = "TICKET-TK002-RAJA-CASABLANCA-2025",
                status     = TicketStatus.VALID
            ),
            TicketModel(
                ticketId   = "TK003",
                eventTitle = "Théâtre Mohamed V",
                eventDate  = "10 Mai 2025",
                eventCity  = "Rabat",
                eventImage = "https://picsum.photos/seed/theatre/400/300",
                category   = "Théâtre",
                price      = 80.0,
                qrCode     = "TICKET-TK003-THEATRE-RABAT-2025",
                status     = TicketStatus.USED
            )
        )
    }
}
*/