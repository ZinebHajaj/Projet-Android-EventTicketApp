package com.dcc.eventticketapp.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcc.eventticketapp.data.Entities.EventModel
import com.dcc.eventticketapp.data.Repository.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(FavoritesViewState())
    val state: StateFlow<FavoritesViewState> = _state

    init { handleIntent(FavoritesIntent.LoadFavorites) }

    fun handleIntent(intent: FavoritesIntent) {
        when (intent) {

            is FavoritesIntent.LoadFavorites -> {
                viewModelScope.launch { loadFavorites() }
            }

            is FavoritesIntent.RemoveFavorite -> {
                viewModelScope.launch {
                    removeFavorite(intent.eventId)
                    loadFavorites() // Recharger après suppression
                }
            }

            is FavoritesIntent.SelectFilter -> {
                val filtered = applyFilter(
                    _state.value.allFavorites,
                    intent.filter
                )
                _state.value = _state.value.copy(
                    selectedFilter = intent.filter,
                    filteredFavorites = filtered
                )
            }

            is FavoritesIntent.ClearAllFavorites -> {
                viewModelScope.launch { clearAllFavorites() }
            }

            is FavoritesIntent.ResetState -> {
                _state.value = FavoritesViewState()
            }

            is FavoritesIntent.ToggleFavorite -> {
                viewModelScope.launch {
                    toggleFavorite(intent.eventId)
                }
            }

            is FavoritesIntent.AddFavorite -> {
                viewModelScope.launch {
                    try {
                        favoritesRepository.addFavorite(intent.eventId)
                        loadFavorites() // Recharger après ajout
                    } catch (e: Exception) {
                        _state.value = _state.value.copy(error = e.message)
                    }
                }
            }
        }
    }

    private suspend fun toggleFavorite(eventId: String) {
        try {
            favoritesRepository.toggleFavorite(eventId)
            loadFavorites() // Recharger UNE SEULE fois ici
        } catch (e: Exception) {
            _state.value = _state.value.copy(error = e.message)
        }
    }

    fun showClearDialog() {
        _state.value = _state.value.copy(showClearDialog = true)
    }

    fun hideClearDialog() {
        _state.value = _state.value.copy(showClearDialog = false)
    }

    // ========== CHARGER DEPUIS REPOSITORY ==========
    private suspend fun loadFavorites() {
        _state.value = _state.value.copy(isLoading = true, error = null)

        try {
            val favorites = favoritesRepository.getFavorites()

            _state.value = _state.value.copy(
                isLoading = false,
                allFavorites = favorites,
                filteredFavorites = favorites,
                error = null
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                isLoading = false,
                error = e.message ?: "Erreur de chargement des favoris",
                allFavorites = emptyList(),
                filteredFavorites = emptyList()
            )
        }
    }

    // ========== SUPPRIMER UN FAVORI ==========
    private suspend fun removeFavorite(eventId: String) {
        try {
            favoritesRepository.removeFavorite(eventId)
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                error = "Erreur suppression: ${e.message}"
            )
        }
    }

    // ========== VIDER TOUS LES FAVORIS ==========
    private suspend fun clearAllFavorites() {
        try {
            favoritesRepository.clearAllFavorites()

            _state.value = _state.value.copy(
                allFavorites = emptyList(),
                filteredFavorites = emptyList(),
                showClearDialog = false
            )
        } catch (e: Exception) {
            _state.value = _state.value.copy(
                error = "Erreur: ${e.message}",
                showClearDialog = false
            )
        }
    }

    private fun applyFilter(
        events: List<EventModel>,
        filter: String
    ): List<EventModel> {
        return if (filter == "Tous") events
        else events.filter { it.category == filter }
    }
}