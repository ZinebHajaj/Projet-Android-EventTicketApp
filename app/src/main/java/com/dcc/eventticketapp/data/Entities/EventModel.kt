package com.dcc.eventticketapp.data.Entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "event")
data class EventModel(

    @SerializedName("id")
    @PrimaryKey
    val id: String = "",

    @SerializedName("title")
    val title: String = "",

    @SerializedName("city")
    val city: String = "",

    @SerializedName("date")
    val date: String = "",

    @SerializedName("imageUrl")
    val imageUrl: String = "",

    @SerializedName("priceStandard")
    val priceStandard: Double = 0.0,

    @SerializedName("category")
    val category: String = "Concerts",

    val organizerId: String = "",      // ID de l'organisateur
    val createdAt: Long = 0,            // Timestamp création
    val updatedAt: Long = 0,            // Timestamp modification

    val isFavorite: Boolean = false
){
    // Détermine si cet événement nécessite une sélection de sièges
    val requiresSeatSelection: Boolean
        get() = category in listOf("Concerts", "Sports", "Théâtre")
}