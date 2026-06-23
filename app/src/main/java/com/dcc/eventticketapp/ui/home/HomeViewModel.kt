package com.dcc.eventticketapp.ui.home

import android.util.Log
import com.dcc.eventticketapp.data.Repository.EventRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcc.eventticketapp.data.Repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: EventRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeViewState())
    val state: StateFlow<HomeViewState> = _state

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadEvents -> {
                viewModelScope.launch { loadEvents() }
            }
            is HomeIntent.ToggleFavorite -> {
                viewModelScope.launch { toggleFavorite(intent.eventId) }
            }
            is HomeIntent.FilterByCategory -> {
                filterByCategory(intent.category)
            }
        }
    }


    fun filterByCategory(category: String) {
        val filtered = if (category == "Tous") {
            _state.value.allEvents
        } else {
            _state.value.allEvents.filter { it.category == category }
        }
        _state.value = _state.value.copy(upcomingEvents = filtered)
    }

    private suspend fun loadEvents() {
        _state.value = _state.value.copy(isLoading = true, error = null)
        try {
            val events = repository.getEvents()

            // MARQUER les favoris
            val favoriteIds = try {
                favoritesRepository.getFavoriteIds()
            } catch (e: Exception) {
                emptySet()
            }

            val eventsWithFavorites = events.map { event ->
                event.copy(isFavorite = favoriteIds.contains(event.id))
            }

            _state.value = HomeViewState(
                isLoading = false,
                upcomingEvents = eventsWithFavorites,
                allEvents = eventsWithFavorites,
                error = null
            )
            /*
            val events = repository.getEvents()
            _state.value = HomeViewState(
                isLoading = false,
                upcomingEvents = events,
                allEvents      = events
            )
             */
        } catch (e: Exception) {
            _state.value = HomeViewState(
                isLoading = false,
                error     = e.message ?: "Erreur de chargement"
            )
        }
    }

    private suspend fun toggleFavorite(eventId: String) {
        try {
            val isNowFavorite = favoritesRepository.toggleFavorite(eventId)

            // Mettre à jour la liste locale
            val updatedEvents = _state.value.upcomingEvents.map { event ->
                if (event.id == eventId) event.copy(isFavorite = isNowFavorite) else event
            }
            val updatedAllEvents = _state.value.allEvents.map { event ->
                if (event.id == eventId) event.copy(isFavorite = isNowFavorite) else event
            }

            _state.value = _state.value.copy(
                upcomingEvents = updatedEvents,
                allEvents = updatedAllEvents
            )
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Error toggling favorite: ${e.message}")
        }
    }
}