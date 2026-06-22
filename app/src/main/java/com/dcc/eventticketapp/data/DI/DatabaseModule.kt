package com.dcc.eventticketapp.data.DI

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dcc.eventticketapp.data.Database.EventDao
import com.dcc.eventticketapp.data.Database.EventDatabase
import com.dcc.eventticketapp.data.Database.FavoriteDao
import com.dcc.eventticketapp.data.preferences.AppPreferencesDataStore
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
            .addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideEventDao(
        database: EventDatabase
    ): EventDao {
        return database.eventDao()
    }

    @Provides
    fun provideFavoriteDao(
        database: EventDatabase
    ): FavoriteDao {
        return database.favoriteDao()
    }

    @Provides
    @Singleton
    fun provideAppPreferencesDataStore(
        @ApplicationContext context: Context
    ): AppPreferencesDataStore = AppPreferencesDataStore(context)
}

// Migration de la version 1 à 2 (ajout des champs organisateur)
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE event ADD COLUMN organizerId TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE event ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE event ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT 0")
    }
}