package com.dcc.eventticketapp.data.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dcc.eventticketapp.data.Entities.EventModel
import com.dcc.eventticketapp.data.Entities.FavoriteEntity

@Database(
    entities = [EventModel::class, FavoriteEntity::class],
    version = 2
)
abstract class EventDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {

        private var INSTANCE: EventDatabase? = null

        fun getInstance(context: Context): EventDatabase {

            if (INSTANCE == null) {

                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java,
                    "event_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }

            return INSTANCE!!
        }
    }
}