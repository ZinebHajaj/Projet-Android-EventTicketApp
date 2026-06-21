package com.dcc.eventticketapp.ui.ticket

sealed class TicketIntent {
    object LoadTickets                        : TicketIntent()
    data class SelectTicket(val ticketId: String) : TicketIntent()
    object DismissTicket                      : TicketIntent()
}