package com.dcc.eventticketapp.ui.profile

sealed class ProfileIntent {

    // Chargement
    object LoadProfile : ProfileIntent()

    // Modifier le nom
    data class NameChanged(val name: String) : ProfileIntent()

    // Modifier l'email
    data class EmailChanged(val email: String) : ProfileIntent()

    // Modifier le téléphone
    data class PhoneChanged(val phone: String) : ProfileIntent()

    // Toggle dark mode
    object ToggleDarkMode : ProfileIntent()

    // Toggle notifications
    object ToggleNotifications : ProfileIntent()

    // Modifier le profil
    object SubmitEditProfile : ProfileIntent()

    // Déconnexion
    object Logout : ProfileIntent()

    // Réinitialiser l'état
    object ResetState : ProfileIntent()
}