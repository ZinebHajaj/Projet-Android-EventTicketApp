package com.dcc.eventticketapp.data.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dcc.eventticketapp.data.Entities.EventModel

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(events: List<EventModel>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(event: EventModel)

    @Query("SELECT * FROM event")
    fun getAll(): List<EventModel>

    @Query("SELECT * FROM event WHERE id = :eventId")
    fun getEventById(eventId: String): EventModel?
}