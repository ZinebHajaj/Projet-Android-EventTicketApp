package com.dcc.eventticketapp.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcc.eventticketapp.data.preferences.AppPreferencesDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppPreferencesState(
    val isDarkMode           : Boolean = false,
    val language             : String  = "fr",
    val notificationsEnabled : Boolean = true
)

@HiltViewModel
class AppPreferencesViewModel @Inject constructor(
    private val dataStore: AppPreferencesDataStore
) : ViewModel() {

    private val _state = MutableStateFlow(AppPreferencesState())
    val state: StateFlow<AppPreferencesState> = _state.asStateFlow()

    init {
        // Charger toutes les préférences sauvegardées au démarrage
        viewModelScope.launch {
            combine(
                dataStore.isDarkMode,
                dataStore.language,
                dataStore.notificationsEnabled,

            ) { darkMode, lang, notifs ->
                AppPreferencesState(
                    isDarkMode           = darkMode,
                    language             = lang,
                    notificationsEnabled = notifs
                )
            }.collect { prefs ->
                _state.value = prefs
            }
        }
    }

    // ── Dark Mode ─────────────────────────────────────────────────
    fun toggleDarkMode() {
        viewModelScope.launch {
            val newValue = !_state.value.isDarkMode
            dataStore.setDarkMode(newValue)  // ← sauvegardé !
        }
    }

    // ── Langue ────────────────────────────────────────────────────
    fun setLanguage(lang: String) {
        viewModelScope.launch {
            dataStore.setLanguage(lang)  // ← sauvegardé !
        }
    }

    // ── Notifications ─────────────────────────────────────────────
    fun toggleNotifications() {
        viewModelScope.launch {
            val newValue = !_state.value.notificationsEnabled
            dataStore.setNotifications(newValue)  // ← sauvegardé !
        }
    }
}