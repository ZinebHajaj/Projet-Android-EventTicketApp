package com.dcc.eventticketapp.ui.favorites

sealed class FavoritesIntent {
    object LoadFavorites : FavoritesIntent()
    data class AddFavorite(val eventId: String) : FavoritesIntent()
    data class RemoveFavorite(val eventId: String) : FavoritesIntent()
    data class ToggleFavorite(val eventId: String) : FavoritesIntent()
    data class SelectFilter(val filter: String) : FavoritesIntent()
    object ClearAllFavorites : FavoritesIntent()
    object ResetState : FavoritesIntent()
}