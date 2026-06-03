package com.dcc.eventticketapp.data.Entities

data class EventModel(
    val id            : String,
    val title         : String  = "",
    val city          : String  = "",
    val date          : String  = "",
    val imageUrl      : String  = "",
    val priceStandard : Double  = 0.0,
    val isFavorite    : Boolean = false,

    val category      : String  = "Concerts"
)