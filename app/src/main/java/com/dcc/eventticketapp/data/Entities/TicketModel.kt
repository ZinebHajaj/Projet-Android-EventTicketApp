package com.dcc.eventticketapp.data.Entities

data class TicketModel(
    val ticketId   : String       = "",
    val eventTitle : String       = "",
    val eventDate  : String       = "",
    val eventCity  : String       = "",
    val eventImage : String       = "",
    val category   : String       = "",
    val price       : Double      = 0.0,
    val currency: String = "MAD",
    val qrCode      : String      = "",
    val status      : TicketStatus = TicketStatus.VALID
)

enum class TicketStatus {
    VALID,
    USED,
    EXPIRED
}