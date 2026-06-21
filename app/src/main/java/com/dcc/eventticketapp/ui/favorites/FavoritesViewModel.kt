package com.dcc.eventticketapp.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    init {
        handleIntent(FavoritesIntent.LoadFavorites)
    }

    fun handleIntent(intent: FavoritesIntent) {
        when (intent) {

            is FavoritesIntent.LoadFavorites -> {
                viewModelScope.launch {

                    _state.value = _state.value.copy(isLoading = true, error = null)

                    try {

                        val favorites = favoritesRepository.getFavorites()

                        _state.value = _state.value.copy(
                            isLoading = false,
                            allFavorites = favorites,
                            filteredFavorites = applyFilter(favorites, _state.value.selectedFilter)
                        )

                    } catch (e: Exception) {

                        _state.value = _state.value.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
            }

            is FavoritesIntent.RemoveFavorite -> {
                viewModelScope.launch {

                    try {

                        favoritesRepository.removeFavorite(intent.eventId)

                        val updated = _state.value.allFavorites.filter { it.id != intent.eventId }

                        _state.value = _state.value.copy(
                            allFavorites = updated,
                            filteredFavorites = applyFilter(updated, _state.value.selectedFilter)
                        )

                    } catch (e: Exception) {
                        _state.value = _state.value.copy(error = e.message)
                    }
                }
            }

            is FavoritesIntent.SelectFilter -> {

                _state.value = _state.value.copy(
                    selectedFilter = intent.filter,
                    filteredFavorites = applyFilter(_state.value.allFavorites, intent.filter)
                )
            }

            is FavoritesIntent.ClearAllFavorites -> {
                viewModelScope.launch {

                    try {

                        favoritesRepository.clearAllFavorites()

                        _state.value = _state.value.copy(
                            allFavorites = emptyList(),
                            filteredFavorites = emptyList(),
                            showClearDialog = false
                        )

                    } catch (e: Exception) {
                        _state.value = _state.value.copy(error = e.message, showClearDialog = false)
                    }
                }
            }

            is FavoritesIntent.ResetState -> {
                _state.value = FavoritesViewState()
            }
        }
    }

    private fun applyFilter(
        favorites: List<com.dcc.eventticketapp.data.Entities.EventModel>,
        filter: String
    ): List<com.dcc.eventticketapp.data.Entities.EventModel> {

        return if (filter == "Tous") {
            favorites
        } else {
            favorites.filter { it.category == filter }
        }
    }

    fun showClearDialog() {
        _state.value = _state.value.copy(showClearDialog = true)
    }

    fun hideClearDialog() {
        _state.value = _state.value.copy(showClearDialog = false)
    }
}