package com.dcc.eventticketapp.ui.profile

import com.dcc.eventticketapp.data.Entities.User

data class ProfileViewState(

    // État général
    val isLoading       : Boolean = false,
    val user            : User?   = null,

    val isAuthenticated: Boolean = false,

    val error           : String? = null,
    val isSuccess       : Boolean = false,
    val isLoggedOut     : Boolean = false,

    // Champs édition profil
    val editName        : String  = "",
    val editEmail       : String  = "",
    val editPhone       : String  = "",

    // Préférences
    val isDarkMode          : Boolean = false,
    val notificationsEnabled: Boolean = true,

    // UI state
    val showEditDialog      : Boolean = false,
    val showLogoutDialog    : Boolean = false,

    // Statistiques
    val reservationsCount   : Int     = 0,
    val favoritesCount      : Int     = 0,
    val eventsCount         : Int     = 0
)