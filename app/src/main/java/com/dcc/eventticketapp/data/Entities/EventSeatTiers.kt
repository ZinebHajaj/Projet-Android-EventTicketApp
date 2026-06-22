package com.dcc.eventticketapp.data.Entities


// Pour les événements avec sièges (Concerts, Sports, Théâtre)
data class SeatBookingInfo(
    val tiers : List<SeatTier> = emptyList()
)

// Pour les événements sans sièges (Ateliers, Autres) -- créneaux horaires
data class TimeSlot(
    val slotId       : String = "",
    val time         : String = "",
    val totalCapacity : Int   = 0,
    val booked        : Int   = 0
) {
    val available : Int
        get() = totalCapacity - booked

    val isFull : Boolean
        get() = available <= 0
}

data class SimpleBookingInfo(
    val slots : List<TimeSlot> = emptyList()
)