package com.dcc.eventticketapp.ui.category

import com.dcc.eventticketapp.data.Entities.Category

data class CategoryViewState(
    val categories       : List<Category> = emptyList(),
    val selectedCategory : String         = "Tous"
)