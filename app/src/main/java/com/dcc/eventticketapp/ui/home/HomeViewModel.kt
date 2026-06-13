package com.dcc.eventticketapp.ui.home

import com.dcc.eventticketapp.data.Repository.EventRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: EventRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomeViewState())
    val state: StateFlow<HomeViewState> = _state

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadEvents -> {
                viewModelScope.launch { loadEvents() }
            }
            is HomeIntent.ToggleFavorite -> {
                toggleFavorite(intent.eventId)
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
            _state.value = HomeViewState(
                isLoading = false,
                upcomingEvents = events,
                allEvents      = events
            )
        } catch (e: Exception) {
            _state.value = HomeViewState(
                isLoading = false,
                error     = e.message ?: "Erreur de chargement"
            )
        }
    }

    private fun toggleFavorite(eventId: String) {
        val updatedEvents = _state.value.upcomingEvents.map { event ->
            if (event.id == eventId)
                event.copy(isFavorite = !event.isFavorite)
            else event
        }
        _state.value = _state.value.copy(upcomingEvents = updatedEvents)
    }
}