package com.dcc.eventticketapp.ui.profile

sealed class ProfileDestination {
    object PersonalInfo : ProfileDestination()
    object Reservations : ProfileDestination()
    object Favorites    : ProfileDestination()
    object Help         : ProfileDestination()
    object Privacy      : ProfileDestination()
    object About        : ProfileDestination()
}