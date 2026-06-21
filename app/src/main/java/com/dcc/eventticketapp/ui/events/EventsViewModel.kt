package com.dcc.eventticketapp.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcc.eventticketapp.data.Entities.EventModel
import com.dcc.eventticketapp.data.Repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val repository: EventRepository
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
                val updated = _state.value.allEvents.map { event ->
                    if (event.id == intent.eventId)
                        event.copy(isFavorite = !event.isFavorite)
                    else event
                }
                val filtered = filterEvents(
                    events   = updated,
                    category = _state.value.selectedCategory,
                    query    = _state.value.searchQuery
                )
                _state.value = _state.value.copy(
                    allEvents      = updated,
                    filteredEvents = filtered
                )
            }

            is EventsIntent.ResetState -> {
                _state.value = EventsViewState()
            }
        }
    }

    private suspend fun loadEvents() {
        _state.value = _state.value.copy(isLoading = true)

        val events = repository.getEvents()
        _state.value = _state.value.copy(
            isLoading      = false,
            allEvents      = events,
            filteredEvents = events
        )

        /*
        // Données simulées — à remplacer par Firebase plus tard
        val fakeEvents = listOf(
            EventModel(
                id = "1",
                title = "Festival Mawazine 2025",
                category = "Concerts",
                city = "Rabat",
                date = "14 Mai 2025 • 20:00",
                imageUrl = "https://picsum.photos/seed/mawazine/400/250",
                priceStandard = 150.0
            ),
            EventModel(
                id = "2",
                title = "Wydad vs Raja — Derby",
                category = "Sports",
                city = "Casablanca",
                date = "22 Juin 2025 • 21:00",
                imageUrl = "https://picsum.photos/seed/derby/400/250",
                priceStandard = 80.0
            ),
            EventModel(
                id = "3",
                title = "TEDx Casablanca 2025",
                category = "Ateliers",
                city = "Casablanca",
                date = "04 Juil. 2025 • 09:00",
                imageUrl = "https://picsum.photos/seed/tedx/400/250",
                priceStandard = 200.0
            ),
            EventModel(
                id = "4",
                title = "Nuit du Stand-Up Comedy",
                category = "Théâtre",
                city = "Marrakech",
                date = "28 Juin 2025 • 21:00",
                imageUrl = "https://picsum.photos/seed/comedy/400/250",
                priceStandard = 120.0
            ),
            EventModel(
                id = "5",
                title = "Concert Gnawa World Music",
                category = "Concerts",
                city = "Essaouira",
                date = "15 Juin 2025 • 19:00",
                imageUrl = "https://picsum.photos/seed/gnawa/400/250",
                priceStandard = 100.0
            ),
            EventModel(
                id = "6",
                title = "Marathon de Casablanca",
                category = "Sports",
                city = "Casablanca",
                date = "10 Oct. 2025 • 07:00",
                imageUrl = "https://picsum.photos/seed/marathon/400/250",
                priceStandard = 50.0
            ),
        )
*/
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