package com.dcc.eventticketapp.data.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dcc.eventticketapp.data.Entities.EventModel

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<EventModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventModel)

    @Query("SELECT * FROM event")
    suspend fun getAll(): List<EventModel>

    @Query("SELECT * FROM event WHERE id = :eventId")
    suspend fun getEventById(eventId: String): EventModel?
}