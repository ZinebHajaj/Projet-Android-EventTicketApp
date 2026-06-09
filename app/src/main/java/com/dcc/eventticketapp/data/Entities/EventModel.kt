package com.dcc.eventticketapp.data.Entities

import com.google.gson.annotations.SerializedName

data class EventModel(
    @SerializedName("id")
    val id            : String  = "",

    @SerializedName("title")
    val title         : String  = "",

    @SerializedName("city")
    val city          : String  = "",

    @SerializedName("date")
    val date          : String  = "",

    @SerializedName("imageUrl")
    val imageUrl      : String  = "",

    @SerializedName("priceStandard")
    val priceStandard : Double  = 0.0,

    @SerializedName("category")
    val category      : String  = "Concerts",

    val isFavorite    : Boolean = false
)