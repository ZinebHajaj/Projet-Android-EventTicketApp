package com.dcc.eventticketapp.data.Preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.currencyDataStore by preferencesDataStore(name = "currency_prefs")

@Singleton
class CurrencyPreference @Inject constructor(
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: Context

) {
    private val CURRENCY_KEY = stringPreferencesKey("selected_currency")

    val currencyFlow: Flow<String> = context.currencyDataStore.data.map { prefs ->
        prefs[CURRENCY_KEY] ?: "MAD"
    }

    suspend fun setCurrency(currency: String) {
        context.currencyDataStore.edit { prefs ->
            prefs[CURRENCY_KEY] = currency
        }
    }
}