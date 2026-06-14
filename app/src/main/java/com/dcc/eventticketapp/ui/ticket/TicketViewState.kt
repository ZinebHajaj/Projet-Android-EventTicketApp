package com.dcc.eventticketapp.ui.ticket

import com.dcc.eventticketapp.data.Entities.TicketModel

data class TicketViewState(
    val isLoading      : Boolean          = false,
    val tickets        : List<TicketModel> = emptyList(),
    val selectedTicket : TicketModel?     = null,  //  billet sélectionné pour QR
    val error          : String?          = null
)