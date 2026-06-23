package com.dcc.eventticketapp.data.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dcc.eventticketapp.data.Entities.EventModel

@Dao
interface EventDao {

    @Query("SELECT * FROM event")
    suspend fun getAll(): List<EventModel>

    @Query("SELECT * FROM event WHERE id = :id")
    suspend fun getEventById(id: String): EventModel?

    @Query("SELECT * FROM event WHERE organizerId = :organizerId")
    suspend fun getMyEvents(organizerId: String): List<EventModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: EventModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<EventModel>)

    @Query("DELETE FROM event WHERE id = :eventId")
    suspend fun deleteById(eventId: String)

    @Query("DELETE FROM event")
    suspend fun deleteAll()
}