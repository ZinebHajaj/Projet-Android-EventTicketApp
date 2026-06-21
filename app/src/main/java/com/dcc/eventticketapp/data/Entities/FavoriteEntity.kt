package com.dcc.eventticketapp.data.Entities

import androidx.room.Entity

@Entity(tableName = "favorite", primaryKeys = ["userId", "eventId"])
data class FavoriteEntity(
    val userId: String,
    val eventId: String,
    val addedAt: Long = System.currentTimeMillis()
)