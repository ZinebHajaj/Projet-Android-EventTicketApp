package com.dcc.eventticketapp.ui.events

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcc.eventticketapp.data.Entities.EventModel
import com.dcc.eventticketapp.data.Repository.EventRepository
import com.dcc.eventticketapp.data.Repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val repository: EventRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EventsViewState())
    val state: StateFlow<EventsViewState> = _state

    init { handleIntent(EventsIntent.LoadEvents) }

    fun handleIntent(intent: EventsIntent) {
        when (intent) {

            is EventsIntent.LoadEvents -> {
                viewModelScope.launch { loadEvents() }
            }

            is EventsIntent.SelectCategory -> {
                val filtered = filterEvents(
                    events   = _state.value.allEvents,
                    category = intent.category,
                    query    = _state.value.searchQuery
                )
                _state.value = _state.value.copy(
                    selectedCategory = intent.category,
                    filteredEvents   = filtered
                )
            }

            is EventsIntent.SearchEvents -> {
                val filtered = filterEvents(
                    events   = _state.value.allEvents,
                    category = _state.value.selectedCategory,
                    query    = intent.query
                )
                _state.value = _state.value.copy(
                    searchQuery    = intent.query,
                    filteredEvents = filtered
                )
            }

            is EventsIntent.ToggleFavorite -> {
                viewModelScope.launch { toggleFavorite(intent.eventId) }
            }

            is EventsIntent.ResetState -> {
                _state.value = EventsViewState()
            }
        }
    }

    private suspend fun loadEvents() {
        _state.value = _state.value.copy(isLoading = true)
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

            _state.value = _state.value.copy(
                isLoading = false,
                allEvents = eventsWithFavorites,
                filteredEvents = eventsWithFavorites,
                error = null
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isLoading = false,
                error = e.message
            )
        }
    }

    private suspend fun toggleFavorite(eventId: String) {
        try {
            val isNowFavorite = favoritesRepository.toggleFavorite(eventId)

            // Mettre à jour allEvents
            val updatedAllEvents = _state.value.allEvents.map { event ->
                if (event.id == eventId) event.copy(isFavorite = isNowFavorite) else event
            }

            // Refiltrer pour mettre à jour filteredEvents aussi
            val filtered = filterEvents(
                events = updatedAllEvents,
                category = _state.value.selectedCategory,
                query = _state.value.searchQuery
            )

            _state.value = _state.value.copy(
                allEvents = updatedAllEvents,
                filteredEvents = filtered
            )
        } catch (e: Exception) {
            Log.e("EventsViewModel", "Error toggling favorite: ${e.message}")
        }
    }

    private fun filterEvents(
        events   : List<EventModel>,
        category : String,
        query    : String
    ): List<EventModel> {
        return events.filter { event ->
            val matchCategory = category == "Tous" || event.category == category
            val matchQuery    = query.isEmpty() ||
                    event.title.contains(query, ignoreCase = true) ||
                    event.city.contains(query, ignoreCase = true)
            matchCategory && matchQuery
        }
    }
}