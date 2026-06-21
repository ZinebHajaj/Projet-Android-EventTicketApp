package com.dcc.eventticketapp.data.Repository

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