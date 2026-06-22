package com.dcc.eventticketapp.data.DI

import android.content.Context
import com.dcc.eventticketapp.data.Database.EventDao
import com.dcc.eventticketapp.data.Database.EventDatabase
import com.dcc.eventticketapp.data.preferences.AppPreferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import com.dcc.eventticketapp.data.Database.FavoriteDao

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): EventDatabase {
        return EventDatabase.getInstance(context)
    }

    @Provides
    fun provideEventDao(
        database: EventDatabase
    ): EventDao {
        return database.eventDao()
    }

    @Provides
    @Singleton
    fun provideAppPreferencesDataStore(
        @ApplicationContext context: Context
    ): AppPreferencesDataStore = AppPreferencesDataStore(context)

    @Provides
    fun provideFavoriteDao(
        database: EventDatabase
    ): FavoriteDao {
        return database.favoriteDao()
    }

}

