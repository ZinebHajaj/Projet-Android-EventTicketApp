package com.dcc.eventticketapp.data.DI

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dcc.eventticketapp.data.Database.EventDao
import com.dcc.eventticketapp.data.Database.EventDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): EventDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            EventDatabase::class.java,
            "event_db"
        )
            .addMigrations(MIGRATION_1_2)  // ← AJOUTER
            .build()
    }

    @Provides
    fun provideEventDao(
        database: EventDatabase
    ): EventDao {
        return database.eventDao()
    }
}

// AJOUTER MIGRATION :
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE event ADD COLUMN organizerId TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE event ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE event ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT 0")
    }
}