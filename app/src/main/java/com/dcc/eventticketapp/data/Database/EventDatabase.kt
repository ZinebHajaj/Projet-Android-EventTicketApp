package com.dcc.eventticketapp.data.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dcc.eventticketapp.data.Entities.EventModel

@Database(
    entities = [EventModel::class],
    version = 1
)
abstract class EventDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao

    companion object {

        private var INSTANCE: EventDatabase? = null

        fun getInstance(context: Context): EventDatabase {

            if (INSTANCE == null) {

                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java,
                    "event_db"
                )
                    //.allowMainThreadQueries()
                    .build()
            }

            return INSTANCE!!
        }
    }
}