package com.dcc.eventticketapp.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences>
        by preferencesDataStore(name = "app_preferences")

@Singleton
class AppPreferencesDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        val DARK_MODE_KEY    = booleanPreferencesKey("dark_mode")
        val LANGUAGE_KEY     = stringPreferencesKey("language")
        val NOTIF_KEY        = booleanPreferencesKey("notifications")

    }

    // ── Dark Mode ─────────────────────────────────────────────────
    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { it[DARK_MODE_KEY] ?: false }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[DARK_MODE_KEY] = enabled }
    }

    // ── Langue ────────────────────────────────────────────────────
    val language: Flow<String> = context.dataStore.data
        .map { it[LANGUAGE_KEY] ?: "fr" }

    suspend fun setLanguage(lang: String) {
        context.dataStore.edit { it[LANGUAGE_KEY] = lang }
    }

    // ── Notifications ─────────────────────────────────────────────
    val notificationsEnabled: Flow<Boolean> = context.dataStore.data
        .map { it[NOTIF_KEY] ?: true }

    suspend fun setNotifications(enabled: Boolean) {
        context.dataStore.edit { it[NOTIF_KEY] = enabled }
    }



}