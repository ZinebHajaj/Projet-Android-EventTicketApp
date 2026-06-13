package com.dcc.eventticketapp.ui.favorites
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcc.eventticketapp.data.Entities.EventModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(FavoritesViewState())
    val state: StateFlow<FavoritesViewState> = _state

    init { handleIntent(FavoritesIntent.LoadFavorites) }

    fun handleIntent(intent: FavoritesIntent) {
        when (intent) {

            is FavoritesIntent.LoadFavorites -> {
                viewModelScope.launch { loadFavorites() }
            }

            is FavoritesIntent.RemoveFavorite -> {
                val updated = _state.value.allFavorites
                    .filter { it.id != intent.eventId }
                val filtered = applyFilter(updated, _state.value.selectedFilter)
                _state.value = _state.value.copy(
                    allFavorites      = updated,
                    filteredFavorites = filtered
                )
            }

            is FavoritesIntent.SelectFilter -> {
                val filtered = applyFilter(
                    _state.value.allFavorites,
                    intent.filter
                )
                _state.value = _state.value.copy(
                    selectedFilter    = intent.filter,
                    filteredFavorites = filtered
                )
            }

            is FavoritesIntent.ClearAllFavorites -> {
                _state.value = _state.value.copy(
                    allFavorites      = emptyList(),
                    filteredFavorites = emptyList(),
                    showClearDialog   = false
                )
            }

            is FavoritesIntent.ResetState -> {
                _state.value = FavoritesViewState()
            }
        }
    }

    fun showClearDialog()  {
        _state.value = _state.value.copy(showClearDialog = true)
    }
    fun hideClearDialog() {
        _state.value = _state.value.copy(showClearDialog = false)
    }

    private suspend fun loadFavorites() {
        _state.value = _state.value.copy(isLoading = true)

        // Données simulées
        val fakeFavorites = listOf(
            EventModel(
                id            = "1",
                title         = "Festival Mawazine 2025",
                category      = "Concerts",
                city          = "Rabat",
                date          = "14 Mai 2025 • 20:00",
                imageUrl      = "https://picsum.photos/seed/mawazine/400/250",
                priceStandard = 150.0,
                isFavorite    = true
            ),
            EventModel(
                id            = "2",
                title         = "Wydad vs Raja — Derby",
                category      = "Sports",
                city          = "Casablanca",
                date          = "22 Juin 2025 • 21:00",
                imageUrl      = "https://picsum.photos/seed/derby/400/250",
                priceStandard = 80.0,
                isFavorite    = true
            ),
            EventModel(
                id            = "3",
                title         = "TEDx Casablanca 2025",
                category      = "Ateliers",
                city          = "Casablanca",
                date          = "04 Juil. 2025 • 09:00",
                imageUrl      = "https://picsum.photos/seed/tedx/400/250",
                priceStandard = 200.0,
                isFavorite    = true
            ),
            EventModel(
                id            = "4",
                title         = "Nuit du Stand-Up Comedy",
                category      = "Théâtre",
                city          = "Marrakech",
                date          = "28 Juin 2025 • 21:00",
                imageUrl      = "https://picsum.photos/seed/comedy/400/250",
                priceStandard = 120.0,
                isFavorite    = true
            ),
            EventModel(
                id            = "5",
                title         = "Concert Gnawa World Music",
                category      = "Concerts",
                city          = "Essaouira",
                date          = "15 Juin 2025 • 19:00",
                imageUrl      = "https://picsum.photos/seed/gnawa/400/250",
                priceStandard = 100.0,
                isFavorite    = true
            ),
            EventModel(
                id            = "6",
                title         = "Marathon de Casablanca",
                category      = "Sports",
                city          = "Casablanca",
                date          = "10 Oct. 2025 • 07:00",
                imageUrl      = "https://picsum.photos/seed/marathon/400/250",
                priceStandard = 50.0,
                isFavorite    = true
            ),
        )

        _state.value = _state.value.copy(
            isLoading         = false,
            allFavorites      = fakeFavorites,
            filteredFavorites = fakeFavorites
        )
    }

    private fun applyFilter(
        events : List<EventModel>,
        filter : String
    ): List<EventModel> {
        return if (filter == "Tous") events
        else events.filter { it.category == filter }
    }
}