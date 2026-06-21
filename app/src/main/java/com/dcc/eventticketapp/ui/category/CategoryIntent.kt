package com.dcc.eventticketapp.ui.category

sealed class CategoryIntent {
    object LoadCategories                          : CategoryIntent()
    data class SelectCategory(val category: String) : CategoryIntent()
}