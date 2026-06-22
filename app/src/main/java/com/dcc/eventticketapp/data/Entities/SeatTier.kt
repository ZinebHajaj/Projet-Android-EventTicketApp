package com.dcc.eventticketapp.data.Entities

data class SeatTier(
    val tierId        : String       = "",
    val name          : String       = "",
    val price         : Double       = 0.0,
    val totalSeats     : Int         = 0,
    val bookedSeats    : Int         = 0,
    val benefits       : List<String> = emptyList()
) {
    val availableSeats : Int
        get() = totalSeats - bookedSeats

    val isSoldOut : Boolean
        get() = availableSeats <= 0
}