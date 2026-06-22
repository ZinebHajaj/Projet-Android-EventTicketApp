package com.dcc.eventticketapp.data.Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dcc.eventticketapp.data.Entities.FavoriteEntity

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favorite: FavoriteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(favorites: List<FavoriteEntity>)

    @Query("DELETE FROM favorite WHERE userId = :userId AND eventId = :eventId")
    fun delete(userId: String, eventId: String)

    @Query("DELETE FROM favorite WHERE userId = :userId")
    fun clearAll(userId: String)

    @Query("SELECT eventId FROM favorite WHERE userId = :userId")
    fun getFavoriteEventIds(userId: String): List<String>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite WHERE userId = :userId AND eventId = :eventId)")
    fun isFavorite(userId: String, eventId: String): Boolean
}