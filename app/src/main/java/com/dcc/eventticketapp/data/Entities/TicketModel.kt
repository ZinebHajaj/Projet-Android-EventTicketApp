package com.dcc.eventticketapp.data.Entities

data class TicketModel(
    val ticketId    : String,
    val eventTitle  : String,
    val eventDate   : String,
    val eventCity   : String,
    val eventImage  : String,
    val category    : String,
    val price       : Double,
    val qrCode      : String,   // contenu du QR code
    val status      : TicketStatus = TicketStatus.VALID
)

enum class TicketStatus {
    VALID,    // billet valide
    USED,     // billet utilisé
    EXPIRED   // billet expiré
}