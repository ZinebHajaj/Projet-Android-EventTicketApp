package com.dcc.eventticketapp.ui.category

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.SportsBasketball
import androidx.compose.material.icons.outlined.TheaterComedy
import androidx.lifecycle.ViewModel
import com.dcc.eventticketapp.data.Entities.Category
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(CategoryViewState())
    val state: StateFlow<CategoryViewState> = _state

    init {
        handleIntent(CategoryIntent.LoadCategories)
    }

    fun handleIntent(intent: CategoryIntent) {
        when (intent) {
            is CategoryIntent.LoadCategories -> loadCategories()
            is CategoryIntent.SelectCategory -> selectCategory(intent.category)
        }
    }

    private fun loadCategories() {
        val categories = listOf(
            Category("Tous",     Icons.Outlined.Apps),
            Category("Concerts", Icons.Outlined.MusicNote),
            Category("Théâtre",  Icons.Outlined.TheaterComedy),
            Category("Sports",   Icons.Outlined.SportsBasketball),
            Category("Ateliers", Icons.Outlined.MenuBook),
            Category("Autres",   Icons.Outlined.MoreHoriz)
        )
        _state.value = _state.value.copy(categories = categories)
    }

    private fun selectCategory(category: String) {
        _state.value = _state.value.copy(selectedCategory = category)
    }
}