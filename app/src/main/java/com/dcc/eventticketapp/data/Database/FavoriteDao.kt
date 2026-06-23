package com.dcc.eventticketapp.data.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dcc.eventticketapp.data.Entities.FavoriteEntity

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite WHERE userId = :userId")
    suspend fun getAll(userId: String): List<FavoriteEntity>

    @Query("SELECT eventId FROM favorite WHERE userId = :userId")
    suspend fun getFavoriteEventIds(userId: String): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(favorites: List<FavoriteEntity>)

    @Query("DELETE FROM favorite WHERE userId = :userId AND eventId = :eventId")
    suspend fun delete(userId: String, eventId: String)

    @Query("DELETE FROM favorite WHERE userId = :userId")
    suspend fun clearAll(userId: String)

    // CORRIGÉ : eventId est String, pas Boolean
    @Query("SELECT EXISTS(SELECT 1 FROM favorite WHERE userId = :userId AND eventId = :eventId)")
    suspend fun isFavorite(userId: String, eventId: String): Boolean
}